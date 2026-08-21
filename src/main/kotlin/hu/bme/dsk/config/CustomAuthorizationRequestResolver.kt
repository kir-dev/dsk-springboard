package hu.bme.dsk.config

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest

const val GOOGLE = "google"
const val AUTHSCH = "authsch"

class CustomAuthorizationRequestResolver(
    repo: ClientRegistrationRepository,
    authorizationRequestBaseUri: String,
) : OAuth2AuthorizationRequestResolver {

    private var defaultResolver: OAuth2AuthorizationRequestResolver? = null
    private val log = LoggerFactory.getLogger(javaClass)

    init {
        defaultResolver = DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri)
    }

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        log.debug("resolve() called with servletPath={}", request.servletPath)
        return when (request.servletPath) {
            "/oauth2/authorization/google" -> {
                log.debug("Detected Google OAuth request")
                var req: OAuth2AuthorizationRequest? = defaultResolver?.resolve(request)
                if (req != null)
                    req = customizeAuthorizationRequest(req, GOOGLE)
                req
            }

            else -> {
                log.debug("Detected AuthSch OAuth request (default path)")
                var req: OAuth2AuthorizationRequest? = defaultResolver?.resolve(request)
                if (req != null)
                    req = customizeAuthorizationRequest(req, AUTHSCH)
                req
            }
        }
    }

    override fun resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest? {
        log.debug("resolve() called with clientRegistrationId={}, path={}", clientRegistrationId, request.servletPath)
        var req: OAuth2AuthorizationRequest? = defaultResolver?.resolve(request, clientRegistrationId)
        if (req != null)
            req = customizeAuthorizationRequest(req, clientRegistrationId)
        return req
    }

    private fun customizeAuthorizationRequest(
        request: OAuth2AuthorizationRequest,
        clientRegistrationId: String
    ): OAuth2AuthorizationRequest? {
        log.debug("customizeAuthorizationRequest called with clientRegistrationId={}", clientRegistrationId)
        log.debug("Current scopes: {}", request.scopes)

        return when (clientRegistrationId) {
            AUTHSCH -> {
                log.debug("Building AUTHSCH authorization request")
                OAuth2AuthorizationRequest
                    .from(request)
                    .build()
            }

            GOOGLE -> {
                log.debug("Building GOOGLE authorization request")
                OAuth2AuthorizationRequest
                    .from(request)
                    .scopes(setOf("profile", "email", "openid"))
                    .build()
            }

            else -> {
                log.debug("Building DEFAULT authorization request for {}", clientRegistrationId)
                OAuth2AuthorizationRequest
                    .from(request)
                    .build()
            }
        }
    }

}