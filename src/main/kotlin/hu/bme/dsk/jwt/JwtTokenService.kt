package hu.bme.dsk.jwt

import hu.bme.dsk.config.StartupPropertyConfig
import hu.bme.dsk.login.LoginUser
import hu.bme.dsk.login.LoginUserPrincipal
import hu.bme.dsk.users.UserRole
import hu.bme.dsk.users.UserService
import hu.bme.dsk.util.getUserEntityFromDatabase
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Service
import java.util.Date

const val JWT_CLAIM_ROLES = "roles"
const val JWT_CLAIM_USERID = "userId"
const val JWT_CLAIM_USERNAME = "userName"
const val JWT_CLAIM_AUTHID = "authId"
const val JWT_CLAIM_GOOGLEID = "googleId"

private const val EXPIRED_OR_INVALID_TOKEN = "Expired or invalid JWT token"

@Service
class JwtTokenService (
    private val startupPropertyConfig: StartupPropertyConfig,
    private val userService: UserService,
) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val secretKey = Keys.hmacShaKeyFor(startupPropertyConfig.secretKey.toByteArray())
    private val parser = Jwts.parser().verifyWith(secretKey).build()

    fun createToken(loginUser: LoginUser): String {
        return createToken(
            userId = loginUser.id,
            userAuthId = loginUser.authId,
            userGoogleId = loginUser.googleId,
            roles = loginUser.roles,
            fullName = loginUser.userName,
        )
    }

    fun createToken(
        userId: Long,
        userAuthId: Long?,
        userGoogleId: Long?,
        roles: List<UserRole>,
        fullName: String,
    ): String {
        val claims = Jwts.claims().subject(userId.toString())
        claims.add(JWT_CLAIM_ROLES, roles.map { it.name })
        claims.add(JWT_CLAIM_USERID, userId.toString())
        claims.add(JWT_CLAIM_USERNAME, fullName)

        val now = Date()
        val validity = Date(now.time + startupPropertyConfig.sessionValiditySeconds * 1000)
        return Jwts.builder()
            .claims(claims.build())
            .issuedAt(now)
            .expiration(validity)
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact()
    }

    @Throws(NoSuchElementException::class)
    fun getAuthentication(token: String): Authentication {
        val parsed = parseToken(token)
        val roles = parsed[JWT_CLAIM_ROLES]?.let { it as List<String> }?.map { UserRole.valueOf(it) } ?: listOf(UserRole.GUEST)
        return UsernamePasswordAuthenticationToken(
            LoginUserPrincipal(
                id = parsed[JWT_CLAIM_USERID]?.toString()?.toLong() ?: 0,
                authId = parsed[JWT_CLAIM_AUTHID]?.toString()?.toLong(),
                googleId = parsed[JWT_CLAIM_GOOGLEID]?.toString()?.toLong(),
                roles = roles,
                userName = parsed[JWT_CLAIM_USERNAME]?.toString() ?: "unnamed",
            ),
            "",
            roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }
        )
    }

    private fun getPermissionsFromClaims(claims: Claims): List<String> {
        val rolesClaim = claims[JWT_CLAIM_ROLES]
        if (rolesClaim !is List<*>) {
            log.error("Invalid JWT format! Roles claim is not a list of strings: {}", rolesClaim)
            return listOf()
        }

        val roles = rolesClaim.filterIsInstance<String>()
        if (roles.size != rolesClaim.size) {
            log.error("Invalid JWT format! Roles claim is not a list of strings: {}", rolesClaim)
        }
        return roles
    }

    fun getUserId(token: String): String {
        return parser.parseSignedClaims(token).payload.subject
    }

    private fun parseToken(token: String) = parser.parseSignedClaims(token).payload

    fun resolveToken(req: HttpServletRequest): String? {
        val jwtCookie = req.cookies?.find { it.name == "jwt" }?.value
        return jwtCookie
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims: Jws<Claims> = parser.parseSignedClaims(token)
            !claims.payload.expiration.before(Date())
        } catch (e: JwtException) {
            throw InvalidJwtAuthenticationException(EXPIRED_OR_INVALID_TOKEN)
        } catch (e: IllegalArgumentException) {
            throw InvalidJwtAuthenticationException(EXPIRED_OR_INVALID_TOKEN)
        } catch (e: io.jsonwebtoken.security.SignatureException) {
            throw InvalidJwtAuthenticationException(EXPIRED_OR_INVALID_TOKEN)
        }
    }

    fun refreshToken(auth: Authentication): String {
        val user = auth.getUserEntityFromDatabase(userService)
        return createToken(
            userId = user.id,
            userAuthId = user.authId,
            userGoogleId = user.googleId,
            roles = user.roles,
            fullName = user.username
        )
    }

}