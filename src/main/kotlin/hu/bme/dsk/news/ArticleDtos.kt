package hu.bme.dsk.news

import hu.bme.dsk.users.UserDto
import java.time.Instant

data class ArticleDto (
    val id: Long,
    val title: String,
    val content: String,
) {
    constructor(article: ArticleEntity) : this(
        id = article.id,
        title = article.title,
        content = article.content
    )
}

data class DetailedArticleDto (
    val id: Long,
    val title: String,
    val content: String,
    val author: UserDto,
    val createdAt: Instant,
) {
    constructor(article: ArticleEntity) : this(
        id = article.id,
        title = article.title,
        content = article.content,
        author = UserDto(article.author),
        createdAt = article.createdAt,
    )
}