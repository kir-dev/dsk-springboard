package hu.bme.dsk.users


import hu.bme.dsk.gymreservation.ReservationEntity
import hu.bme.dsk.news.ArticleEntitiy
import hu.bme.dsk.rentings.RentingEntity
import jakarta.persistence.*

@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: Long,

    var username: String,

    var authId: Long?,

    var googleId: Long?,

    var roles: MutableList<UserRole> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var reservations: MutableList<ReservationEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var rentings: MutableList<RentingEntity> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], orphanRemoval = true)
    var articles: MutableList<ArticleEntitiy> = mutableListOf(),
)
