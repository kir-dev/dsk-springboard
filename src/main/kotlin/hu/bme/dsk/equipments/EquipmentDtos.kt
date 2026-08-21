package hu.bme.dsk.equipments

import hu.bme.dsk.sports.SportDto

data class EquipmentDto(
    val id: Long,
    val name: String,
    val description: String,
    val count: Int,
    val availableCount: Int,
    val imageLink: String,
) {
    constructor(equipment: EquipmentEntity) : this(
        id = equipment.id,
        name = equipment.name,
        description = equipment.description,
        count = equipment.count,
        availableCount = equipment.availableCount,
        imageLink = equipment.imageLink,
    )
}

data class DetailedEquipmentDto(
    val id: Long,
    val name: String,
    val description: String,
    val count: Int,
    val availableCount: Int,
    val imageLink: String,
    val sportDto: SportDto,
) {
    constructor(equipment: EquipmentEntity) : this(
        id = equipment.id,
        name = equipment.name,
        description = equipment.description,
        count = equipment.count,
        availableCount = equipment.availableCount,
        imageLink = equipment.imageLink,
        sportDto = SportDto(equipment.sport)
    )
}
