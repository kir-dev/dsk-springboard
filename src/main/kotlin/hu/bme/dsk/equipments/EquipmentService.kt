package hu.bme.dsk.equipments

import hu.bme.dsk.sports.SportRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class EquipmentService (
    private val equipmentRepository: EquipmentRepository,
    private val sportRepository: SportRepository
) {
    @Transactional(readOnly = false)
    fun create(equipmentDto: CreateEquipmentDto, sportId: UUID): DetailedEquipmentDto {
        val sport = sportRepository.findByIdOrNull(sportId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Sport with id $sportId not found")

        val equipment = EquipmentEntity(
            name = equipmentDto.name,
            description = equipmentDto.description,
            count = equipmentDto.count,
            availableCount = equipmentDto.count,
            imageLink = equipmentDto.imageLink,
            sport = sport
        )

        val savedEquipment = equipmentRepository.save(equipment)

        sport.equipments.add(savedEquipment)

        return DetailedEquipmentDto(savedEquipment)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<DetailedEquipmentDto> {
        return equipmentRepository.findAllWithSport().map { DetailedEquipmentDto(it) }
    }

    @Transactional(readOnly = true)
    fun find(id: UUID): DetailedEquipmentDto {
        val equipment = equipmentRepository.findByIdWithSport(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment with id $id not found")

        return DetailedEquipmentDto(equipment)
    }

    @Transactional(readOnly = false)
    fun update(id: UUID, dto: UpdateEquipmentDto, sportId: UUID) : DetailedEquipmentDto {
        val equipment = equipmentRepository.findByIdWithSport(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Equipment with id $id not found")

        val newAvailableCount = equipment.availableCount + dto.count - equipment.count

        if (newAvailableCount < 0) throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory cannot be negative")

        equipment.apply {
            name = dto.name
            description = dto.description
            count = dto.count
            availableCount = newAvailableCount
            imageLink = dto.imageLink
        }

        if (sportId != equipment.sport.id) {
            equipment.sport.equipments.remove(equipment)

            val newSport = sportRepository.findByIdOrNull(sportId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Sport with id $sportId not found")

            newSport.equipments.add(equipment)

            equipment.sport = newSport
        }

        val updatedEquipment = equipmentRepository.save(equipment)
        return DetailedEquipmentDto(updatedEquipment)
    }

    @Transactional(readOnly = false)
    fun delete(id: UUID) {
        val equipment = equipmentRepository.findByIdWithSport(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Equipment with id $id not found")

        equipment.sport.equipments.remove(equipment)
        equipmentRepository.delete(equipment)
    }
}