package hu.bme.dsk.equipments

import hu.bme.dsk.sports.SportRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class EquipmentService (
    private val equipmentRepository: EquipmentRepository,
    private val sportRepository: SportRepository
) {
    @Transactional
    fun createEquipment(equipmentDto: CreateEquipmentDto, sportId: UUID): DetailedEquipmentDto {
        val sport = sportRepository.findById(sportId)
            .orElseThrow { RuntimeException("Sport with id $sportId not found") }

        val equipment = EquipmentEntity(
            name = equipmentDto.name,
            description = equipmentDto.description,
            count = equipmentDto.count, // Fixed a tiny typo here!
            availableCount = equipmentDto.count,
            imageLink = equipmentDto.imageLink,
            sport = sport
        )

        val savedEquipment = equipmentRepository.save(equipment)

        sport.equipments.add(savedEquipment)
        sportRepository.save(sport)

        return DetailedEquipmentDto(savedEquipment)
    }

    @Transactional(readOnly = true)
    fun getAllEquipments(): List<DetailedEquipmentDto> {
        return equipmentRepository.findAll().map { DetailedEquipmentDto(it) }
    }

    @Transactional(readOnly = true)
    fun getEquipmentById(id: UUID): DetailedEquipmentDto {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { RuntimeException("Equipment with id $id not found") }

        return DetailedEquipmentDto(equipment)
    }

    @Transactional
    fun updateEquipment(id: UUID, dto: UpdateEquipmentDto, sportId: UUID) : DetailedEquipmentDto {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { RuntimeException("Equipment with id $id not found") }

        val newAvailableCount = equipment.availableCount + dto.count - equipment.count

        if (newAvailableCount < 0) throw RuntimeException("Inventory cannot be negative")

        equipment.apply {
            name = dto.name
            description = dto.description
            count = dto.count
            availableCount = newAvailableCount
            imageLink = dto.imageLink
        }

        if (sportId != equipment.sport.id) {
            equipment.sport.equipments.remove(equipment)

            val newSport = sportRepository.findById(sportId)
                .orElseThrow { RuntimeException("Sport with id $sportId not found") }

            newSport.equipments.add(equipment)

            equipment.sport = newSport
        }

        val updatedEquipment = equipmentRepository.save(equipment)
        return DetailedEquipmentDto(updatedEquipment)
    }

    @Transactional
    fun deleteEquipment(id: UUID) {
        val equipment = equipmentRepository.findById(id)
            .orElseThrow { RuntimeException("Equipment with id $id not found") }

        equipment.sport.equipments.remove(equipment)
        equipmentRepository.delete(equipment)
    }
}