package hu.bme.dsk.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): Optional<UserEntity>

    fun findByGoogleId(googleId: String): UserEntity?

    fun findByAuthId(authId: Long): UserEntity?

    fun findAllByRole(roles: List<UserRole>): MutableList<UserEntity>

}