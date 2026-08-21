package hu.bme.dsk.users

import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import java.util.Optional

class UserService {

    fun findByAuthId(authId: String): Optional<UserEntity> {
        // TODO Implementation for finding user by Authsch ID
        return Optional.empty()
    }
    fun findByGoogleId(googleId: String): Optional<UserEntity> {
        // TODO Implementation for finding user by Google ID
        return Optional.empty()
    }
    fun generateUserEntity(profile: AuthschProfileResponse): UserEntity {
        // TODO Implementation for generating user entity from Authsch profile
        return UserEntity(1, "test")
    }
    fun generateUserEntity(profile: GoogleUserInfoResponse): UserEntity {
        // TODO Implementation for generating user entity from Google user info
        return UserEntity(1, "test")
    }

    fun save(user: UserEntity): UserEntity {
        // TODO Implementation for saving user entity
        return user
    }

    fun getById(name: String): UserEntity {
        // TODO Implementation for finding user by ID
        return UserEntity(1, "test")
    }

    fun findByInternalId(name: String): Optional<UserEntity> {
        // TODO("Not yet implemented")
        return Optional.empty()
    }

}