package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentDto
import hu.bme.dsk.equipments.UpdateEquipmentDto
import hu.bme.dsk.users.UserDto
import java.time.Instant
import java.util.UUID

data class RentingDto (
    val id: UUID,
    val startTime: Instant,
    val endTime: Instant,
    val rentingStatus: RentingStatus,
) {
    constructor(renting: RentingEntity) : this(
        id = renting.id,
        startTime = renting.startTime,
        endTime = renting.endTime,
        rentingStatus = renting.rentingStatus,
    )
}

data class DetailedRentingDto(
    val id: UUID,
    val startTime: Instant,
    val endTime: Instant,
    val rentingStatus: RentingStatus,
    val createdAt: Instant,
    val creatingUser: UserDto,
    val equipmentRenting: List<EquipmentRentingDto>,
    val issuingUser: UserDto?,
    val returningUser: UserDto?,
) {
    constructor(renting: RentingEntity) : this(
        id = renting.id,
        startTime = renting.startTime,
        endTime = renting.endTime,
        rentingStatus = renting.rentingStatus,
        createdAt = renting.createdAt,
        creatingUser = UserDto(renting.creatingUser),
        equipmentRenting = renting.equipmentRenting.map { EquipmentRentingDto(it) },
        issuingUser = renting.issuingUser?.let { UserDto(it) },
        returningUser = renting.returningUser?.let { UserDto(it) }
    )
}

data class CreateRentingDto(
    val startTime: Instant,
    val endTime: Instant,
    val creatingUserId: UUID,
    val issuingUserId: UUID?,
    val returningUserId: UUID?,
    val equipments: List<CreateEquipmentRentingDto>,
)

data class UpdateRentingDto(
    val startTime: Instant,
    val endTime: Instant,
    val creatingUserId: UUID,
    val issuingUserId: UUID?,
    val returningUserId: UUID?,
    val equipments: List<UpdateEquipmentRentingDto>,
)