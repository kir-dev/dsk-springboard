package hu.bme.dsk.rentings

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/api/rentings")
class RentingController(
    private val rentingService: RentingService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody dto: CreateRentingDto) : DetailedRentingDto {
        return rentingService.create(dto)
    }

    @GetMapping("/{rentingId}")
    @ResponseStatus(HttpStatus.OK)
    fun getRenting(@PathVariable rentingId: UUID) : DetailedRentingDto {
        return rentingService.find(rentingId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllRentings(): List<DetailedRentingDto> {
        return rentingService.findAll()
    }

    @PatchMapping("/{rentingId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateRenting(@PathVariable rentingId: UUID, @Valid @RequestBody dto: UpdateRentingDto) : DetailedRentingDto {
        return rentingService.update(rentingId, dto)
    }

    @DeleteMapping("/{rentingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRenting(@PathVariable rentingId: UUID) {
        return rentingService.delete(rentingId)
    }
}