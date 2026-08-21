package hu.bme.dsk.sports

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class SportService (
    private val sportRepository: SportRepository,
) {
    @Transactional
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
        val sport = sportRepository.findById(id)
            .orElseThrow { RuntimeException("Sport with id $id not found") }

        return DetailedSportDto(sport)
    }

    @Transactional
    fun updateSport(id: UUID, dto: UpdateSportDto): DetailedSportDto {
        val sport = sportRepository.findById(id)
            .orElseThrow { RuntimeException("Sport with id $id not found") }

        sport.name = dto.name

        val updatedSport = sportRepository.save(sport)
        return DetailedSportDto(updatedSport)
    }

    @Transactional
    fun deleteSport(id: UUID) {
        val sport = sportRepository.findById(id)
            .orElseThrow { RuntimeException("Sport with id $id not found") }

        sportRepository.delete(sport)
    }
}