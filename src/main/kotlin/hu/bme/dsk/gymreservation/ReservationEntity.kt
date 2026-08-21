package hu.bme.dsk.gymreservation


import hu.bme.dsk.rentings.RentingStatus
import hu.bme.dsk.users.UserEntity
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import kotlin.time.Instant

@Entity
@Table(name = "gym_reservation")
data class ReservationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    @CreationTimestamp
    @Column(updatable = false)
    val createdAt: Instant,

    var startTime: Instant,

    var endTime: Instant,

    @ManyToOne(fetch = FetchType.LAZY)
    var creatingUser: UserEntity,

    var reservationStatus: ReservationStatus,
)
