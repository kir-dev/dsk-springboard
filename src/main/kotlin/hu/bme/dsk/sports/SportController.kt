package hu.bme.dsk.sports

import hu.bme.dsk.equipments.DetailedEquipmentDto
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID


@RestController
@RequestMapping("/sports")
class SportController (
    private val sportService: SportService
){
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody dto: CreateSportDto) : DetailedSportDto {
        return sportService.create(dto)
    }

    @GetMapping("/{sportId}")
    @ResponseStatus(HttpStatus.OK)
    fun get(@PathVariable sportId: UUID) : DetailedSportDto {
        return sportService.find(sportId)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAll() : List<SportDto> {
        return sportService.findAll()
    }

    @PatchMapping("/{sportId}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable sportId: UUID, @Valid @RequestBody dto: UpdateSportDto) : DetailedSportDto {
        return sportService.update(sportId, dto)
    }

    @DeleteMapping("/{sportId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable sportId: UUID) {
        sportService.delete(sportId)
    }
}