package hu.bme.dsk.login

import hu.bme.dsk.config.StartupPropertyConfig
import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import hu.bme.dsk.users.CreateUserDto
import hu.bme.dsk.users.DetailedUserDto
import hu.bme.dsk.users.UserEntity
import hu.bme.dsk.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.PlatformTransactionManager

@Service
class LoginService(
    private val users: UserService,
    private val startupPropertyConfig: StartupPropertyConfig,
    private val transactionManager: PlatformTransactionManager,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun fetchAuthschUserEntity(profile: AuthschProfileResponse): DetailedUserDto {
        try {
            val user = users.findByAuthIdOrNull(profile.internalId)
            if (user != null) return user

            log.info("No user found with authId ${profile.internalId}. Creating a new user.")
            log.info("Creating new user ${profile.email} with authId ${profile.internalId}.")

            return users.generateUser(profile)
        }
        catch (e: Exception) {
            log.error("Error fetching or creating user entity for profile ${profile.internalId}: ${e.message}", e)
            throw LoginRejectedException("Sikertelen bejelentkezési kísérelt")
        }
    }

    fun fetchGoogleUserEntity(profile: GoogleUserInfoResponse): DetailedUserDto {
        try {
            val user = users.findByGoogleIdOrNull(profile.internalId)
            if (user != null) return user

            log.info("No user found with authId ${profile.internalId}. Creating a new user.")
            log.info("Creating new user ${profile.email} with authId ${profile.internalId}.")

            return users.generateUser(profile)
        }
        catch (e: Exception) {
            log.error("Error fetching or creating user entity for profile ${profile.internalId}: ${e.message}", e)
            throw LoginRejectedException("Sikertelen bejelentkezési kísérelt")
        }
    }
}