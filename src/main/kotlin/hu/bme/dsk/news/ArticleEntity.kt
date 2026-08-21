package hu.bme.dsk.news

import hu.bme.dsk.users.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "articles")
data class ArticleEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    var author: UserEntity,

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: Instant = Instant.now(),

    var title: String,

    var content: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArticleEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , author = $author , title = $title , content = $content)"
    }
}
