package hu.bme.dsk.sports

import hu.bme.dsk.equipments.EquipmentDto
import java.util.UUID

data class SportDto(
    val id: UUID,
    val name: String,
) {
    constructor(sport: SportEntity) : this(
        id = sport.id,
        name = sport.name,
    )
}

data class DetailedSportDto(
    val id: UUID,
    val name: String,
    val equipments: List<EquipmentDto>,
) {
    constructor(sport: SportEntity) : this(
        id = sport.id,
        name = sport.name,
        equipments = sport.equipments.map{ EquipmentDto(it) },
    )
}

data class CreateSportDto(
    val name: String,
)

data class UpdateSportDto(
    val name: String,
)