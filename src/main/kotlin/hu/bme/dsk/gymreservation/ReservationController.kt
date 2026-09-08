package hu.bme.dsk.gymreservation

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.time.Instant
import java.util.UUID


@RestController
@RequestMapping("/api/gym-reservations")
class ReservationController (
    private val reservationService: ReservationService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody dto: CreateReservationDto) : DetailedReservationDto {
        return reservationService.createReservation(dto)
    }

    @PatchMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable reservationId: UUID, @Valid @RequestBody dto: UpdateReservationDto) : DetailedReservationDto {
        return reservationService.updateReservation(reservationId, dto)
    }

    @GetMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable reservationId: UUID) : DetailedReservationDto {
        return reservationService.getReservationById(reservationId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll(): List<DetailedReservationDto> {
        return reservationService.getAllReservations()
    }

    @GetMapping("/between")
    @ResponseStatus(HttpStatus.OK)
    fun getAllStartingTime(@RequestParam start: Instant, @RequestParam end: Instant) : List<DetailedReservationDto> {
        return reservationService.getAllStartingBetween(start, end)
    }

    @DeleteMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable reservationId: UUID) {
        reservationService.deleteReservation(reservationId)
    }
}