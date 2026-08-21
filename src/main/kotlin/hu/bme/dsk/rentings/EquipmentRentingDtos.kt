package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentDto
import java.util.UUID

data class EquipmentRentingDto(
    val id: UUID,
    val count: Int
) {
    constructor(equipmentRenting: EquipmentRentingEntity) : this(
        id = equipmentRenting.id,
        count = equipmentRenting.count
    )
}

data class DetailedEquipmentRentingDto(
    val id: UUID,
    val count: Int,
    val equipment: EquipmentDto,
    val renting: RentingDto,
) {
    constructor(equipmentRenting: EquipmentRentingEntity) : this(
        id = equipmentRenting.id,
        count = equipmentRenting.count,
        equipment = EquipmentDto(equipmentRenting.equipment),
        renting = RentingDto(equipmentRenting.renting),
    )
}

data class CreateEquipmentRentingDto(
    val count: Int,
    val equipmentId: UUID,
)

data class UpdateEquipmentRentingDto(
    val count: Int,
    val equipmentId: UUID,
)