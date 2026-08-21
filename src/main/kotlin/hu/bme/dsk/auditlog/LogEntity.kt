package hu.bme.dsk.auditlog

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "audit_log")
data class LogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var userId: Long,

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: Instant = Instant.now(),

    var message: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is LogEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , userId = $userId , message = $message )"
    }
}

