package hu.bme.dsk.users

import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun findByAuthId(authId: String): DetailedUserDto {
        val user = userRepository.findByAuthId(authId)
            .orElseThrow { RuntimeException("User with id $authId not found") }
        return DetailedUserDto(user)
    }

    @Transactional(readOnly = true)
    fun findByGoogleId(googleId: String): DetailedUserDto {
        val user = userRepository.findByGoogleId(googleId)
            .orElseThrow { RuntimeException("User with id $googleId not found") }
        return DetailedUserDto(user)
    }

    @Transactional
    fun generateUserEntity(profile: AuthschProfileResponse): DetailedUserDto {
        val user = UserEntity(
            username = profile.displayName ?: throw RuntimeException("Username not set"),
            authId = profile.internalId,
        )

        val savedUser = userRepository.save(user)
        return DetailedUserDto(savedUser)
    }

    @Transactional
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
        .orElseThrow { RuntimeException("User with name $username not found") }

        return DetailedUserDto(user)
    }

    @Transactional(readOnly = true)
    fun getById(id: UUID): DetailedUserDto {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User with id $id not found") }

        return DetailedUserDto(user)
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserDto> {
        return userRepository.findAll().map { UserDto(it) }
    }

    @Transactional(readOnly = true)
    fun findByInternalId(id: String) : DetailedUserDto {
        val user = userRepository.findByAuthIdOrGoogleId(id, id)
            .orElseThrow { RuntimeException("User with id $id not found") }

        return DetailedUserDto(user)
    }

    @Transactional
    fun updateUser(id: UUID, dto: UpdateUserDto): DetailedUserDto {
        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User with id $id not found") }

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