package hu.bme.dsk.sports
    
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class SportService (
    private val sportRepository: SportRepository,
) {
    @Transactional(readOnly = false)
    fun createSport(dto: CreateSportDto): DetailedSportDto {
        val sport = SportEntity(
            name = dto.name,
            equipments = mutableListOf(),
        )
        val savedSport = sportRepository.save(sport)
        return DetailedSportDto(savedSport)
    }

    @Transactional(readOnly = true)
    fun getAllSports(): List<SportDto> {
        return sportRepository.findAll().map { SportDto(it) }
    }

    @Transactional(readOnly = true)
    fun getSportById(id: UUID): DetailedSportDto {
        val sport = sportRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Sport with id ${id} not found")

        return DetailedSportDto(sport)
    }

    @Transactional(readOnly = false)
    fun updateSport(id: UUID, dto: UpdateSportDto): DetailedSportDto {
        val sport = sportRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Sport with id ${id} not found")

        sport.name = dto.name

        val updatedSport = sportRepository.save(sport)
        return DetailedSportDto(updatedSport)
    }

    @Transactional(readOnly = false)
    fun deleteSport(id: UUID) {
        val sport = sportRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Sport with id ${id} not found")

        sportRepository.delete(sport)
    }
}