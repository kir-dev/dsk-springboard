package hu.bme.dsk.auditlog

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import kotlin.time.Instant

@Entity
@Table(name = "audit_log")
data class LogEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var userId: Long,

    @CreationTimestamp
    @Column(updatable = false)
    var createdAt: Instant,
)

