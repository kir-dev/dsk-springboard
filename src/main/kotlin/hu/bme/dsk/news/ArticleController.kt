package hu.bme.dsk.news

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/news")
class ArticleController(
    private val articleService: ArticleService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody dto: CreateArticleDto, @RequestParam authorId: UUID) : DetailedArticleDto {
        return articleService.create(dto, authorId)
    }

    @GetMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun getArticle(@PathVariable articleId: UUID) : DetailedArticleDto {
        return articleService.find(articleId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllArticles(): List<DetailedArticleDto> {
        return articleService.findAll()
    }

    @PatchMapping("/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateArticle(@PathVariable articleId: UUID, @Valid @RequestBody dto: UpdateArticleDto) : DetailedArticleDto {
        return articleService.updateArticle(articleId, dto)
    }

    @DeleteMapping("/{articleId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteArticle(@PathVariable articleId: UUID) {
        return articleService.delete(articleId)
    }
}