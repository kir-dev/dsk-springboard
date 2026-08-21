package hu.bme.dsk.login.authsch

import hu.bme.dsk.login.LoginUser
import hu.bme.dsk.users.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import java.io.Serializable
import java.security.Principal

class LoginAuthschUser(
    override val id: Long,
    override val authId: Long?,
    override val googleId: Long?,
    override var roles: List<UserRole>,
    override val userName: String,
    authorities: List<GrantedAuthority>,
    ) : DefaultOAuth2User(authorities, mapOf("external_id" to authId) as Map<String, Any>, "external_id"), LoginUser, Principal, Serializable {

    override fun getName() = authId.toString()

    override fun hasRole(permission: UserRole): Boolean {
        return roles.contains(permission)
    }

    override fun hasAllRoles(permission: List<UserRole>): Boolean {
        return permission.all { hasRole(it) }
    }

    override fun hasAnyRoles(permission: List<UserRole>): Boolean {
        return permission.any { hasRole(it) }
    }
}