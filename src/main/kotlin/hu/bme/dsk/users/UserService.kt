package hu.bme.dsk.users

import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun findByAuthId(authId: String): DetailedUserDto {
        val user = userRepository.findByAuthId(authId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $authId not found")

        return DetailedUserDto(user)
    }

    @Transactional(readOnly = true)
    fun findByGoogleId(googleId: String): DetailedUserDto {
        val user = userRepository.findByGoogleId(googleId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $googleId not found")

        return DetailedUserDto(user)
    }

    @Transactional(readOnly = false)
    fun generateUserEntity(profile: AuthschProfileResponse): DetailedUserDto {
        val user = UserEntity(
            username = profile.displayName ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "User name not found"),
            authId = profile.internalId,
        )

        val savedUser = userRepository.save(user)
        return DetailedUserDto(savedUser)
    }

    @Transactional(readOnly = false)
    fun generateUserEntity(profile: GoogleUserInfoResponse): DetailedUserDto {
        val user = UserEntity(
            username = profile.name,
            googleId = profile.internalId,
        )

        val savedUser = userRepository.save(user)
        return DetailedUserDto(savedUser)
    }

    @Transactional
    fun save(user: UserEntity): DetailedUserDto {
        return DetailedUserDto( userRepository.save(user) )
    }

    @Transactional(readOnly = true)
    fun getByUsername(username: String): DetailedUserDto {
        val user = userRepository.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with name $username not found")

        return DetailedUserDto(user)
    }

    @Transactional(readOnly = true)
    fun getById(id: UUID): DetailedUserDto {
        val user = userRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id not found")

        return DetailedUserDto(user)
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll().map { UserDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByInternalId(id: String) : DetailedUserDto {
        val user = userRepository.findByAuthIdOrGoogleId(id, id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id not found")

        return DetailedUserDto(user)
    }

    @Transactional
    fun updateUser(id: UUID, dto: UpdateUserDto): DetailedUserDto {
        val user = userRepository.findByIdOrNull(id)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with id $id not found")

        user.apply {
            username = dto.username
            roles = dto.roles.toMutableList()
        }

        val updatedUser = userRepository.save(user)
        return DetailedUserDto(updatedUser)
    }

    @Transactional
    fun deleteUserByInternalId(id: String) {
        val user = findByInternalId(id)

        userRepository.deleteById(user.id)
    }
}