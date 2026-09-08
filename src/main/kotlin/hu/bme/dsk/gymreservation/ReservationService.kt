package hu.bme.dsk.gymreservation

import hu.bme.dsk.users.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import java.time.Instant

@Service
class ReservationService (
    private val reservationRepository: ReservationRepository,
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = false)
    fun createReservation(dto: CreateReservationDto) : DetailedReservationDto {
        val user = userRepository.findByIdOrNull(dto.creatingUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id ${dto.creatingUserId} not found")

        val reservation = ReservationEntity(
            startTime = dto.startTime,
            endTime = dto.endTime,
            reservationStatus = dto.reservationStatus,
            creatingUser = user
        )

        val savedReservation = reservationRepository.save(reservation)
        return DetailedReservationDto(savedReservation)
    }

    @Transactional(readOnly = false)
    fun updateReservation(id: UUID, dto: UpdateReservationDto) : DetailedReservationDto {
        val reservation = reservationRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with id ${id} not found")

        val user = userRepository.findByIdOrNull(dto.creatingUserId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id ${dto.creatingUserId} not found")

        reservation.apply {
            startTime = dto.startTime
            endTime = dto.endTime
            reservationStatus = dto.reservationStatus
            creatingUser = user
        }

        val savedReservation = reservationRepository.save(reservation)
        return DetailedReservationDto(savedReservation)
    }

    @Transactional(readOnly = true)
    fun getReservationById(id: UUID) : DetailedReservationDto {
        val reservation = reservationRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation with id ${id} not found")

        return DetailedReservationDto(reservation)
    }

    @Transactional(readOnly = true)
    fun getAllReservations(): List<DetailedReservationDto> {
        return reservationRepository.findAll().map { DetailedReservationDto(it) }
    }

    @Transactional(readOnly = false)
    fun deleteReservation(id: UUID) {
        reservationRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    fun getAllStartingBetween(startTime: Instant, endTime: Instant): List<DetailedReservationDto> {
        return reservationRepository.findAllStartTimeBetween(startTime, endTime).map { DetailedReservationDto(it) }
    }
}