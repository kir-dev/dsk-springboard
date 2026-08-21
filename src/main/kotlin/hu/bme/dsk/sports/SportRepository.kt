package hu.bme.dsk.sports

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SportRepository : JpaRepository<SportEntity, Long> {
    fun findAllOrderByTitleDesc(pageable: Pageable): List<SportEntity>
}