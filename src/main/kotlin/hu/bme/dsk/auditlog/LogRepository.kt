package hu.bme.dsk.auditlog

import hu.bme.dsk.equipments.DetailedEquipmentDto
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface LogRepository : JpaRepository<LogEntity, UUID> {

    @EntityGraph(attributePaths = ["user"])
    @Query("SELECT l FROM LogEntity l")
    fun findAllWithUser(): List<LogEntity>

}