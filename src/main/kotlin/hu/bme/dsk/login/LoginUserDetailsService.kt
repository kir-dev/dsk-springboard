package hu.bme.dsk.login

import hu.bme.dsk.users.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException

@Service
class LoginUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with username $username not found")

        return LoginUserDetails(user)
    }
}