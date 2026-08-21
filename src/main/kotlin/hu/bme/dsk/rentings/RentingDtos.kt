package hu.bme.dsk.rentings

import hu.bme.dsk.users.UserDto
import java.time.Instant

data class RentingDto (
    val id: Long,
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
    val id: Long,
    val startTime: Instant,
    val endTime: Instant,
    val rentingStatus: RentingStatus,
    val createdAt: Instant,
    val creatingUser: UserDto,
    val equipmentRenting: List<EquipmentRentingDto>,
    val issuingUser: UserDto,
    val returningUser: UserDto,
) {
    constructor(renting: RentingEntity) : this(
        id = renting.id,
        startTime = renting.startTime,
        endTime = renting.endTime,
        rentingStatus = renting.rentingStatus,
        createdAt = renting.createdAt,
        creatingUser = UserDto(renting.creatingUser),
        equipmentRenting = renting.equipmentRenting.map { EquipmentRentingDto(it) },
        issuingUser = UserDto(renting.issuingUser),
        returningUser = UserDto(renting.returningUser)
    )
}