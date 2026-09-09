package hu.bme.dsk.news

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


@Repository
interface ArticleRepository : JpaRepository<ArticleEntity, UUID> {

    @EntityGraph(attributePaths = ["author"])
    fun findAllByAuthor_Id(authorId: UUID): List<ArticleEntity>

    @EntityGraph(attributePaths = ["author"])
    fun findByOrderByCreatedAtDesc(pageable: Pageable): List<ArticleEntity>
}