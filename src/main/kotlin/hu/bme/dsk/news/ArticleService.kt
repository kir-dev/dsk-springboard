package hu.bme.dsk.news

import hu.bme.dsk.users.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun createArticle(dto: CreateArticleDto, authorId: UUID) : DetailedArticleDto {
        val author = userRepository.findById(authorId)
                .orElseThrow { RuntimeException("Author user with id $authorId not found") }

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
    fun getArticleById(articleId: UUID) : DetailedArticleDto {
        val article = articleRepository.findById(articleId)
        .orElseThrow { RuntimeException("Article with id $articleId not found") }

        return DetailedArticleDto(article)
    }

    @Transactional(readOnly = true)
    fun getAllArticles() : List<DetailedArticleDto> {
        return articleRepository.findAll().map{ DetailedArticleDto(it) }
    }

    @Transactional(readOnly = true)
    fun getAllArticlesFromAuthor(authorId: UUID) : List<DetailedArticleDto> {
        return articleRepository.findAllByAuthor_Id(authorId).map{ DetailedArticleDto(it) }
    }

    @Transactional
    fun updateArticle(id: UUID, dto: UpdateArticleDto) : DetailedArticleDto {
        val article = articleRepository.findById(id)
            .orElseThrow { RuntimeException("Article with id $id not found") }

        article.apply {
            this.title = dto.title
            this.content = dto.content
        }

        val savedArticle = articleRepository.save(article)
        return DetailedArticleDto(savedArticle)
    }

    @Transactional
    fun deleteArticle(id: UUID) {
        val article = articleRepository.findById(id)
            .orElseThrow { RuntimeException("Article with id $id not found") }

        article.author.articles.remove(article)

        articleRepository.delete(article)
    }
}