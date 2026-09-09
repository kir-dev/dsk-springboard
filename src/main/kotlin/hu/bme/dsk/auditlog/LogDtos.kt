package hu.bme.dsk.auditlog

import hu.bme.dsk.users.UserDto
import java.util.UUID
import java.time.Instant

data class LogDto(
    val id : UUID,
    val createdAt: Instant,
    val message: String,
) {
    constructor(log: LogEntity) : this(
        id = log.id,
        createdAt = log.createdAt,
        message = log.message,
    )
}

data class DetailedLogDto(
    val id : UUID,
    val user : UserDto,
    val createdAt: Instant,
    val message: String,
) {
    constructor(log: LogEntity) : this(
        id = log.id,
        user = UserDto(log.user),
        createdAt = log.createdAt,
        message = log.message,
    )
}

data class CreateLogDto(
    val userId : UUID,
    val message: String,
)