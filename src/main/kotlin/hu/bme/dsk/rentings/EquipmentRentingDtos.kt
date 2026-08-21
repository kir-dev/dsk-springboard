package hu.bme.dsk.rentings

import hu.bme.dsk.equipments.EquipmentDto

data class EquipmentRentingDto(
    val id: Long,
    val count: Int
) {
    constructor(equipmentRenting: EquipmentRentingEntity) : this(
        id = equipmentRenting.id,
        count = equipmentRenting.count
    )
}

data class DetailedEquipmentRentingDto(
    val id: Long,
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
