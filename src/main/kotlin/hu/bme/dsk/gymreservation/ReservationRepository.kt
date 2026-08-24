package hu.bme.dsk.gymreservation

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface ReservationRepository : JpaRepository<ReservationEntity, UUID> {
    fun findAllByStartTimeIsAfterOrderByStartTimeAsc(time: Instant): List<ReservationEntity>

    fun findAllByEndTimeIsAfterOrderByEndTimeAsc(time: Instant): List<ReservationEntity>

    fun findAllByReservationStatusIs(
        reservationStatus: ReservationStatus
    ): List<ReservationEntity>

    fun findAllByCreatingUserId(
        creatingUserId: UUID
    ): List<ReservationEntity>

    fun findAllByReservationStatusIsAndStartTimeIsAfterOrderByStartTimeAsc(
        reservationStatus: ReservationStatus,
        time: Instant
    ): List<ReservationEntity>

    fun findAllByReservationStatusIsAndEndTimeIsBeforeOrderByEndTimeAsc(
        reservationStatus: ReservationStatus,
        time: Instant
    ): List<ReservationEntity>

    fun findAllByStartTimeBetween(
        startTime: Instant,
        endTime: Instant
    ) : List<ReservationEntity>
}