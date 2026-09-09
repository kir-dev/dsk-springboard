package hu.bme.dsk.auditlog

import hu.bme.dsk.users.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID


@Service
class LogService(
    private val logRepository: LogRepository,
    private val userRepository : UserRepository,
) {

    @Transactional(readOnly = false)
    fun create(dto: CreateLogDto) : DetailedLogDto {
        val user = userRepository.findByIdOrNull(dto.userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id ${dto.userId} not found")

        val log = LogEntity(
            user = user,
            message = dto.message,
        )

        return DetailedLogDto(logRepository.save(log))
    }

    @Transactional(readOnly = true)
    fun findAll() : List<DetailedLogDto> {
        val logs = logRepository.findAllWithUser()
        return logs.map { DetailedLogDto(it) }
    }

    @Transactional(readOnly = true)
    fun find(id : UUID) : DetailedLogDto {
        val log = logRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Log with id $id not found")

        return DetailedLogDto(log)
    }

    @Transactional(readOnly = false)
    fun delete(id : UUID) {
        val log = logRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Log with id $id not found")

        logRepository.delete(log)
    }
}