package hu.bme.dsk.gymreservation


import hu.bme.dsk.users.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity
@Table(name = "gym_reservation")
data class ReservationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: Instant = Instant.now(),

    var startTime: Instant,

    var endTime: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    var creatingUser: UserEntity,

    var reservationStatus: ReservationStatus,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ReservationEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , startTime = $startTime , endTime = $endTime)"
    }
}
