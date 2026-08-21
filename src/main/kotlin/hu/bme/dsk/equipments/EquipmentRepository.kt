package hu.bme.dsk.equipments

import hu.bme.dsk.rentings.RentingEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.awt.print.Pageable
import java.util.UUID

interface EquipmentRepository : JpaRepository<EquipmentEntity, UUID> {
    fun findAllBySportId(sportId: Long, pageable: Pageable): List<RentingEntity>
    fun findAllOrderByAvailableCountDesc(pageable: Pageable): List<RentingEntity>
}