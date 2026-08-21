package hu.bme.dsk.sports

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SportRepository : JpaRepository<SportEntity, UUID> {
    fun findAllOrderByTitleDesc(pageable: Pageable): List<SportEntity>
}