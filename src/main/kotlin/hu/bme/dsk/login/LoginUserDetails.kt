package hu.bme.dsk.login

import hu.bme.dsk.users.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class LoginUserDetails(
    val userEntity: UserEntity,
) : UserDetails, LoginUser by userEntity as LoginUser {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return userEntity.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }
    }

    override fun getPassword(): String? = null

    override fun getUsername(): String = userEntity.username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}