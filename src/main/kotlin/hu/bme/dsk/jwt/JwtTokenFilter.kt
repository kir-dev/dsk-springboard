package hu.bme.dsk.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException

class JwtTokenFilter(
    private val jwtTokenService: JwtTokenService
) : GenericFilterBean() {
    private val log = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, filterChain: FilterChain) {
        val httpRequest = req as HttpServletRequest
        if (httpRequest.servletPath.startsWith("/api/")) {
            val token: String? = jwtTokenService.resolveToken(httpRequest)
            try {
                if (token != null && jwtTokenService.validateToken(token)) {
                    val auth: Authentication = jwtTokenService.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = auth
                }
            } catch (e: Exception) {
                log.warn("Invalid token: {} user cannot be resolved because: {}", token, e.message)
            }
        }
        filterChain.doFilter(req, res)
    }
}