package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentRepository
import hu.bme.dsk.users.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class RentingService(
    private val rentingRepository: RentingRepository,
    private val equipmentRepository: EquipmentRepository,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = false)
    fun create(dto: CreateRentingDto) : DetailedRentingDto {
        val creatingUser = userRepository.findByIdOrNull(dto.creatingUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id ${dto.creatingUserId} not found")

        val issuingUser = dto.issuingUserId?.let {
            userRepository.findByIdOrNull(it)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id ${dto.issuingUserId} not found")
        }

        val returningUser = dto.returningUserId?.let {
            userRepository.findByIdOrNull(it)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id ${dto.returningUserId} not found")
        }

        val renting = RentingEntity(
            startTime = dto.startTime,
            endTime = dto.endTime,
            creatingUser = creatingUser,
            rentingStatus = RentingStatus.REQUESTED,
            issuingUser = issuingUser,
            returningUser = returningUser,
        )

        applyEquipmentRequest(renting, dto.equipments)

        val savedRenting = rentingRepository.save(renting)
        return DetailedRentingDto(savedRenting)
    }

    @Transactional(readOnly = false)
    fun updateStatus(id: UUID, userId: UUID, rentingStatus: RentingStatus) : DetailedRentingDto {
        val renting = rentingRepository.findByIdWithDetails(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Renting with id $id not found")

        if (rentingStatus == RentingStatus.ISSUED) {
            renting.issuingUser = userRepository.findByIdOrNull(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Issuing user with id $userId not found")
        }
        if (rentingStatus == RentingStatus.RETURNED) {
            renting.returningUser = userRepository.findByIdOrNull(userId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Returning user with id $userId not found")

            renting.equipmentRenting.forEach { it.equipment.availableCount += it.count }
        }

        renting.apply { this.rentingStatus = rentingStatus }

        val savedRenting = rentingRepository.save(renting)
        return DetailedRentingDto(savedRenting)
    }

    @Transactional(readOnly = false)
    fun update(id: UUID, dto: UpdateRentingDto) : DetailedRentingDto {
        val renting = rentingRepository.findByIdWithDetails(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Renting with id $id not found")

        renting.apply {
            startTime = dto.startTime
            endTime = dto.endTime
        }

        renting.equipmentRenting.forEach { it.equipment.availableCount += it.count }
        renting.equipmentRenting.clear()

        applyEquipmentRequest(renting, dto.equipments)

        val savedRenting = rentingRepository.save(renting)
        return DetailedRentingDto(savedRenting)
    }

    @Transactional(readOnly = true)
    fun findAll(): List<DetailedRentingDto> {
        return rentingRepository.findAllWithDetails().map { DetailedRentingDto(it) }
    }

    @Transactional(readOnly = true)
    fun find(id: UUID): DetailedRentingDto {
        val renting = rentingRepository.findByIdWithDetails(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Renting with id $id not found")

        return DetailedRentingDto(renting)
    }

    @Transactional(readOnly = false)
    fun delete(id: UUID) {
        val renting = rentingRepository.findByIdWithDetails(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Renting with id $id not found")

        for (e in renting.equipmentRenting) {
            e.equipment.availableCount += e.count
        }

        rentingRepository.delete(renting)
    }

    private fun applyEquipmentRequest(renting: RentingEntity, requests: List<EquipmentRequestDto>) {
        for (request in requests) {
            val equipment = equipmentRepository.findByIdOrNull(request.equipmentId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Equipment with id ${request.equipmentId} not found")

            if (equipment.availableCount < request.count)
                throw ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough stock for equipment: ${equipment.name}. Requested: ${request.count}, Available: ${equipment.availableCount}")

            equipment.availableCount -= request.count

            val equipmentRenting = EquipmentRentingEntity(
                count = request.count,
                equipment = equipment,
                renting = renting,
            )

            renting.equipmentRenting.add(equipmentRenting)
        }
    }
}