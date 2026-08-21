package hu.bme.dsk.users

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?

    fun findByGoogleId(googleId: String): UserEntity?

    fun findByAuthId(authId: Long): UserEntity?

    fun findAllByRole(roles: List<UserRole>): MutableList<UserEntity>

}