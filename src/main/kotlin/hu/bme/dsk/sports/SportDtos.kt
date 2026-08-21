package hu.bme.dsk.sports

import hu.bme.dsk.equipments.EquipmentDto

data class SportDto(
    val id: Long,
    val name: String,
) {
    constructor(sport: SportEntity) : this(
        id = sport.id,
        name = sport.name,
    )
}

data class DetailedSportDto(
    val id: Long,
    val name: String,
    val equipments: List<EquipmentDto>,
) {
    constructor(sport: SportEntity) : this(
        id = sport.id,
        name = sport.name,
        equipments = sport.equipments.map{ EquipmentDto(it) },
    )
}
