package hu.bme.dsk.gymreservation

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface ReservationRepository : JpaRepository<ReservationEntity, Long> {
    fun findAllByStartTimeIsAfterOrderByStartTimeAsc(time: Instant, pageable: Pageable): List<ReservationEntity>

    fun findAllByEndTimeIsAfterOrderByEndTimeAsc(time: Instant, pageable: Pageable): List<ReservationEntity>

    fun findAllByReservationStatusIs(
        reservationStatus: ReservationStatus,
        pageable: Pageable
    ): Page<ReservationEntity>

    fun findAllByCreatingUserId(
        creatingUserId: Long,
        pageable: Pageable
    ): Page<ReservationEntity>

    fun findAllByReservationStatusIsAndStartTimeIsAfterOrderByStartTimeAsc(
        reservationStatus: ReservationStatus,
        time: Instant,
        pageable: Pageable
    ): Page<ReservationEntity>

    fun findAllByReservationStatusIsAndEndTimeIsBeforeOrderByEndTimeAsc(
        reservationStatus: ReservationStatus,
        time: Instant,
        pageable: Pageable
    ): Page<ReservationEntity>
}