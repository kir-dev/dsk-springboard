package hu.bme.dsk.login

import hu.bme.dsk.users.UserEntity
import hu.bme.dsk.users.UserRepository
import hu.bme.dsk.users.UserRole
import java.util.UUID

interface LoginUser {
    val id: UUID
    val authId: String?
    val googleId: String?
    var roles: List<UserRole>
    val userName: String

    fun hasAnyRoles(permission: List<UserRole>): Boolean
    fun hasAllRoles(permission: List<UserRole>): Boolean
    fun hasRole(permission: UserRole): Boolean

    fun refresh(loginUser: LoginUser) {
        roles = loginUser.roles
    }
}

fun LoginUser.asUserEntity(userRepository: UserRepository): UserEntity {
    if (this is UserEntity) return this
    return userRepository.findById(this.id).orElseThrow()
}