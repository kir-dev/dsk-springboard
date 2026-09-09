package hu.bme.dsk.rentings

import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RentingRepository : JpaRepository<RentingEntity, UUID> {

    @EntityGraph(attributePaths = ["creatingUser", "equipmentRenting", "equipmentRenting.equipment", "issuingUser", "returningUser"])
    @Query("SELECT r FROM RentingEntity r")
    fun findAllWithDetails(): List<RentingEntity>

    @EntityGraph(attributePaths = ["creatingUser", "equipmentRenting", "equipmentRenting.equipment", "issuingUser", "returningUser"])
    @Query("SELECT r FROM RentingEntity r WHERE r.id = :id")
    fun findByIdWithDetails(id: UUID): RentingEntity?
}