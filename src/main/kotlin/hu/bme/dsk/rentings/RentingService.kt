package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentRepository
import hu.bme.dsk.users.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class RentingService(
    private val rentingRepository: RentingRepository,
    private val equipmentRepository: EquipmentRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createRenting(dto: CreateRentingDto) : DetailedRentingDto {
        val creatingUser = userRepository.findById(dto.creatingUserId)
            .orElseThrow{RuntimeException("User with id ${dto.creatingUserId} not found") }

        val issuingUser = dto.issuingUserId?.let {
            userRepository.findById(it)
                .orElseThrow { RuntimeException("User with id ${dto.issuingUserId} not found") }
        }

        val returningUser = dto.returningUserId?.let {
            userRepository.findById(it)
                .orElseThrow { RuntimeException("User with id ${dto.returningUserId} not found") }
        }

        val renting = RentingEntity(
            startTime = dto.startTime,
            endTime = dto.endTime,
            creatingUser = creatingUser,
            rentingStatus = RentingStatus.REQUESTED,
            issuingUser = issuingUser,
            returningUser = returningUser,
        )

        for (request in dto.equipments) {
            val equipment = equipmentRepository.findById(request.equipmentId)
                .orElseThrow { RuntimeException("Equipment with id ${request.equipmentId} not found") }

            if (equipment.availableCount < request.count)
                throw RuntimeException("Not enough stock for equipment: ${equipment.name}. Requested: ${request.count}, Available: ${equipment.availableCount}")

            equipment.availableCount -= request.count

            val equipmentRenting = EquipmentRentingEntity(
                count = request.count,
                equipment = equipment,
                renting = renting,
            )

            renting.equipmentRenting.add(equipmentRenting)
        }

        val savedRenting = rentingRepository.save(renting)
        return DetailedRentingDto(savedRenting)
    }

    @Transactional
    fun updateRentingStatus(id: UUID, userId: UUID, rentingStatus: RentingStatus) : DetailedRentingDto {
        val renting = rentingRepository.findById(id)
            .orElseThrow { RuntimeException("Renting with id $id not found") }

        if (rentingStatus == RentingStatus.ISSUED) {
            renting.issuingUser = userRepository.findById(userId)
                .orElseThrow { RuntimeException("Issuing user with id $userId not found") }
        }
        if (rentingStatus == RentingStatus.RETURNED) {
            renting.returningUser = userRepository.findById(userId)
                .orElseThrow { RuntimeException("Returning user with id $userId not found") }

            renting.equipmentRenting.forEach { it -> it.equipment.availableCount += it.count }
        }

        renting.apply { this.rentingStatus = rentingStatus }

        val savedRenting = rentingRepository.save(renting)
        return DetailedRentingDto(savedRenting)
    }

    @Transactional
    fun updateRenting(id: UUID, dto: UpdateRentingDto) : DetailedRentingDto {
        val renting = rentingRepository.findById(id)
            .orElseThrow { RuntimeException("Renting with id $id not found") }

        renting.apply {
            startTime = dto.startTime
            endTime = dto.endTime
        }

        for (old in renting.equipmentRenting) {
            old.equipment.availableCount += old.count
        }

        renting.equipmentRenting.clear()

        for (request in dto.equipments) {
            val equipment = equipmentRepository.findById(request.equipmentId)
                .orElseThrow { RuntimeException("Equipment with id ${request.equipmentId} not found") }

            if (equipment.availableCount < request.count)
                throw RuntimeException("Not enough stock for equipment: ${equipment.name}. Requested: ${request.count}, Available: ${equipment.availableCount}")

            equipment.availableCount -= request.count

            val equipmentRenting = EquipmentRentingEntity(
                count = request.count,
                equipment = equipment,
                renting = renting,
            )

            renting.equipmentRenting.add(equipmentRenting)
        }

        val savedRenting = rentingRepository.save(renting)
        return DetailedRentingDto(savedRenting)
    }

    @Transactional(readOnly = true)
    fun getAllRentings(): List<DetailedRentingDto> {
        return rentingRepository.findAll().map { DetailedRentingDto(it) }
    }

    @Transactional(readOnly = true)
    fun getRentingById(id: UUID): DetailedRentingDto {
        return DetailedRentingDto(rentingRepository.findById(id).get())
    }

    @Transactional
    fun deleteRenting(id: UUID) {
        val renting = rentingRepository.findById(id)
            .orElseThrow { RuntimeException("Renting with id $id not found") }

        for (e in renting.equipmentRenting) {
            e.equipment.availableCount += e.count
        }

        rentingRepository.delete(renting)
    }
}