package hu.bme.dsk.users


import hu.bme.dsk.gymreservation.ReservationEntity
import hu.bme.dsk.news.ArticleEntity
import hu.bme.dsk.rentings.RentingEntity
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var username: String,

    var authId: Long? = null,

    var googleId: Long? = null,

    var roles: MutableList<UserRole> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reservations: MutableList<ReservationEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var rentings: MutableList<RentingEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var articles: MutableList<ArticleEntity> = mutableListOf(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserEntity) return false
        if (id != other.id) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun toString(): String {
        return this::class.simpleName + "(username = $username , roles = $roles)"
    }
}
