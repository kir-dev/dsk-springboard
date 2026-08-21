package hu.bme.dsk.news

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ArticleRepository : JpaRepository<ArticleEntitiy, Long> {
    fun findAllByAuthor(author: String): List<ArticleEntitiy>

    fun findByOrderByCreatedAtDesc(pageable: Pageable): List<ArticleEntitiy>
}