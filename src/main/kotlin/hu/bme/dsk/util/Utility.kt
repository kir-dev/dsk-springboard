package hu.bme.dsk.util

import hu.bme.dsk.login.LoginUser
import hu.bme.dsk.users.DetailedUserDto
import hu.bme.dsk.users.UserEntity
import hu.bme.dsk.users.UserService
import org.springframework.security.core.Authentication
import java.util.UUID
import kotlin.jvm.optionals.getOrNull

fun Authentication.getUser(): LoginUser {
    return this.principal as LoginUser
}

fun Authentication?.getUserOrNull(): LoginUser? {
    return if (this == null) null else (this.principal as? LoginUser)
}

fun Authentication.getUserEntityFromDatabase(userService: UserService): DetailedUserDto {
    return userService.getByUsername(this.name)
}

fun Authentication?.getUserEntityFromDatabaseOrNull(userService: UserService): DetailedUserDto? {
    val userIdString = this?.name ?: return null

    return userService.findByInternalIdOrNull(userIdString)
}

fun String.toUUIDOrNull(): UUID? {
    return try {
        UUID.fromString(this)
    }
    catch (e : IllegalArgumentException) {
        null
    }
}