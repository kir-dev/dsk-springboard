package hu.bme.dsk.login

import hu.bme.dsk.users.UserRole
import java.io.Serializable
import java.security.Principal
import java.util.UUID

data class LoginUserPrincipal(
    override val id: UUID,
    override val authId: String?,
    override val googleId: String?,
    override var roles: List<UserRole>,
    override val userName: String,
): Serializable, Principal, LoginUser {

    override fun getName() = id.toString()

    override fun hasRole(permission: UserRole): Boolean {
        return roles.contains(permission)
    }

    override fun hasAnyRoles(permission: List<UserRole>): Boolean {
        return permission.any { hasRole(it) }
    }

    override fun hasAllRoles(permission: List<UserRole>): Boolean {
        return permission.all { hasRole(it) }
    }
}

