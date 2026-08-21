package hu.bme.dsk.gymreservation

import hu.bme.dsk.users.UserDto
import java.time.Instant

data class ReservationDto(
    val id: Long,
    val startTime: Instant,
    val endTime: Instant,
    val reservationStatus: ReservationStatus,
) {
    constructor(reservation: ReservationEntity) : this(
        id = reservation.id,
        startTime = reservation.startTime,
        endTime = reservation.endTime,
        reservationStatus = reservation.reservationStatus,
    )
}

data class DetailedReservationDto(
    val id: Long,
    val startTime: Instant,
    val endTime: Instant,
    val reservationStatus: ReservationStatus,
    val createdAt: Instant,
    val creatingUser: UserDto,
) {
    constructor(reservation: ReservationEntity) : this(
        id = reservation.id,
        startTime = reservation.startTime,
        endTime = reservation.endTime,
        reservationStatus = reservation.reservationStatus,
        createdAt = reservation.createdAt,
        creatingUser = UserDto(reservation.creatingUser),
    )
}
