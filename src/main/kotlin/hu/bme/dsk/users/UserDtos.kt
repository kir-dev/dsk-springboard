package hu.bme.dsk.users

import hu.bme.dsk.gymreservation.ReservationDto
import hu.bme.dsk.news.ArticleDto
import hu.bme.dsk.rentings.RentingDto


data class UserDto(
    val id : Long,
    val username : String,
    val authId : Long?,
    val googleId : Long?,
    val roles : List<UserRole>?,
) {
    constructor(user: UserEntity) : this(
        id = user.id,
        username = user.username,
        authId = user.authId,
        googleId = user.googleId,
        roles = user.roles,
    )
}

data class DetailedUserDto(
    val id : Long,
    val username: String,
    val authId : Long?,
    val googleId : Long?,
    val roles : List<UserRole>?,
    val reservations: List<ReservationDto>,
    val rentings: List<RentingDto>,
    val articles: List<ArticleDto>,
) {
    constructor(user: UserEntity) : this(
        id = user.id,
        username = user.username,
        authId = user.authId,
        googleId = user.googleId,
        roles = user.roles,
        reservations = user.reservations.map { ReservationDto(it) },
        rentings = user.rentings.map { RentingDto(it) },
        articles = user.articles.map { ArticleDto(it) },
    )
}
