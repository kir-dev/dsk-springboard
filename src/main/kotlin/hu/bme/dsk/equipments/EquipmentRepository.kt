package hu.bme.dsk.equipments

import hu.bme.dsk.rentings.RentingEntity
import java.awt.print.Pageable

interface EquipmentRepository {
    fun findAllBySportId(sportId: Long, pageable: Pageable): List<RentingEntity>

    fun findAllOrderByAvailableCountDesc(pageable: Pageable): List<RentingEntity>
}