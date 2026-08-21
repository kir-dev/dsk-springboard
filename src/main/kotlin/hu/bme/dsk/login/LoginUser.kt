package hu.bme.dsk.login

import hu.bme.dsk.users.UserEntity
import hu.bme.dsk.users.UserRepository
import hu.bme.dsk.users.UserRole

interface LoginUser {
    val id: Long
    val authId: Long?
    val googleId: Long?
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