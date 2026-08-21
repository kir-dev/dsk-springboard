package hu.bme.dsk.jwt

import org.springframework.security.core.AuthenticationException

class InvalidJwtAuthenticationException(e: String) : AuthenticationException(e)
