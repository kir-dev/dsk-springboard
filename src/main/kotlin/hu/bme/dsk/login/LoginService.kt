package hu.bme.dsk.login

import hu.bme.dsk.config.StartupPropertyConfig
import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import hu.bme.dsk.users.UserEntity
import hu.bme.dsk.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.transaction.PlatformTransactionManager

class LoginService(
    private val users: UserService,
    private val startupPropertyConfig: StartupPropertyConfig,
    private val transactionManager: PlatformTransactionManager,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun fetchAuthschUserEntity(profile: AuthschProfileResponse): UserEntity {
        try {
            val user = users.findByAuthId(profile.internalId)
                .orElseGet {
                    log.info("No user found with authId ${profile.internalId}. Creating a new user.")
                    log.info("Creating new user ${profile.email} with authId ${profile.internalId}.")
                    return@orElseGet users.save(users.generateUserEntity(profile))
                }

            return user
        } catch (e: Exception) {
            log.error("Error fetching or creating user entity for profile ${profile.internalId}: ${e.message}", e)
            throw LoginRejectedException("Sikertelen bejelentkezési kísérelt")
        }
    }

    fun fetchGoogleUserEntity(profile: GoogleUserInfoResponse): UserEntity {
        try {
            val user = users.findByGoogleId(profile.internalId)
                .orElseGet {
                    log.info("No user found with Google ID ${profile.internalId}. Creating a new user.")
                    log.info("Creating new user ${profile.email} with Google ID ${profile.internalId}.")
                    return@orElseGet users.save(users.generateUserEntity(profile))
                }

            return user
        } catch (e: Exception) {
            log.error("Error fetching or creating user entity for Google profile ${profile.internalId}: ${e.message}", e)
            throw LoginRejectedException("Sikertelen bejelentkezési kísérelt")
        }
    }
}