package hu.bme.dsk.news

import hu.bme.dsk.users.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = false)
    fun create(dto: CreateArticleDto, authorId: UUID) : DetailedArticleDto {
        val author = userRepository.findByIdOrNull(authorId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Author user with id $authorId not found")

        val article = ArticleEntity(
            title = dto.title,
            content = dto.content,
            author = author,
        )

        author.articles.add(article)

        val savedArticle = articleRepository.save(article)
        return DetailedArticleDto(savedArticle)
    }

    @Transactional(readOnly = true)
    fun find(articleId: UUID) : DetailedArticleDto {
        val article = articleRepository.findByIdOrNull(articleId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Article with id $articleId not found")

        return DetailedArticleDto(article)
    }

    @Transactional(readOnly = true)
    fun findAll() : List<DetailedArticleDto> {
        return articleRepository.findAll().map{ DetailedArticleDto(it) }
    }

    @Transactional(readOnly = true)
    fun getAllArticlesFromAuthor(authorId: UUID) : List<DetailedArticleDto> {
        return articleRepository.findAllByAuthor_Id(authorId).map{ DetailedArticleDto(it) }
    }

    @Transactional(readOnly = false)
    fun updateArticle(id: UUID, dto: UpdateArticleDto) : DetailedArticleDto {
        val article = articleRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Article with id $id not found")

        article.apply {
            this.title = dto.title
            this.content = dto.content
        }

        val savedArticle = articleRepository.save(article)
        return DetailedArticleDto(savedArticle)
    }

    @Transactional(readOnly = false)
    fun delete(id: UUID) {
        val article = articleRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND,"Article with id $id not found")

        article.author.articles.remove(article)

        articleRepository.delete(article)
    }
}