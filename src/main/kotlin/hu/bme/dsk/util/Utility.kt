package hu.bme.dsk.util

import hu.bme.dsk.login.LoginUser
import hu.bme.dsk.users.DetailedUserDto
import hu.bme.dsk.users.UserEntity
import hu.bme.dsk.users.UserService
import org.springframework.security.core.Authentication
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

fun Authentication?.getUserEntityFromDatabaseOrNull(userService: UserService): UserEntity? {
    return if (this == null) null else userService.findByInternalId(this.name).getOrNull()
}
