package hu.bme.dsk.login

import hu.bme.dsk.users.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

class LoginUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }
        return LoginUserDetails(user)
    }
}