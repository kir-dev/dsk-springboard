package hu.bme.dsk.equipments

import hu.bme.dsk.rentings.RentingEntity
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface EquipmentRepository : JpaRepository<EquipmentEntity, UUID> {

    @EntityGraph(attributePaths = ["sport"])
    @Query("SELECT e FROM EquipmentEntity e WHERE e.id = :id")
    fun findByIdWithSport(id: UUID): EquipmentEntity?

    @EntityGraph(attributePaths = ["sport"])
    @Query("SELECT e FROM EquipmentEntity e")
    fun findAllWithSport(): List<EquipmentEntity>

    @EntityGraph(attributePaths = ["sport"])
    @Query("SELECT e FROM EquipmentEntity e WHERE e.sport.id = :sportId")
    fun findAllBySportId(sportId: UUID): List<EquipmentEntity>

    @EntityGraph(attributePaths = ["sport"])
    fun findAllOrderByAvailableCountDesc(): List<EquipmentEntity>
}