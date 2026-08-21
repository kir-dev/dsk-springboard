package hu.bme.dsk.login.google

import hu.bme.dsk.login.LoginUser
import hu.bme.dsk.users.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import java.io.Serializable
import java.security.Principal

class LoginGoogleUser(
    override val id: Long,
    override val authId: Long?,
    override val googleId: Long?,
    override var roles: List<UserRole>,
    override val userName: String,
    authorities: List<GrantedAuthority>,
    idToken: OidcIdToken,
) : DefaultOidcUser(authorities, idToken), LoginUser, Principal, Serializable {
    override fun getName() = googleId.toString()

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