package hu.bme.dsk.auditlog

import hu.bme.dsk.users.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "audit_log")
class LogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

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
        return this::class.simpleName + "(id = $id , message = $message )"
    }
}

