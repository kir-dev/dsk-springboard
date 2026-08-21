package hu.bme.dsk.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {
    fun findByUsername(username: String): Optional<UserEntity>

    fun findByGoogleId(googleId: String): Optional<UserEntity>

    fun findByAuthId(authId: String): Optional<UserEntity>

    fun findByAuthIdOrGoogleId(authId: String, googleId: String): Optional<UserEntity>

    fun findAllByRole(roles: List<UserRole>): MutableList<UserEntity>

}