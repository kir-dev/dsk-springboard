package hu.bme.dsk.equipments

import hu.bme.dsk.gymreservation.DetailedReservationDto
import hu.bme.dsk.users.DetailedUserDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/equipments")
class EquipmentController(
    private val equipmentService: EquipmentService,
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody dto: CreateEquipmentDto, @RequestBody sportId: UUID) : DetailedEquipmentDto {
        return equipmentService.create(dto, sportId)
    }

    @GetMapping("/{equipmentId}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable equipmentId: UUID) : DetailedEquipmentDto {
        return equipmentService.find(equipmentId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll() : List<DetailedEquipmentDto> {
        return equipmentService.findAll()
    }

    @PatchMapping("/{equipmentId}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable equipmentId: UUID, @Valid @RequestBody dto: UpdateEquipmentDto, @RequestBody sportId: UUID) : DetailedEquipmentDto {
        return equipmentService.update(equipmentId, dto, sportId)
    }

    @DeleteMapping("/{equipmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable equipmentId: UUID) {
        return equipmentService.delete(equipmentId)
    }

}