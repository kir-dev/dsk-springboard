package hu.bme.dsk.config

import jakarta.servlet.http.HttpServletRequest
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

    init {
        defaultResolver = DefaultOAuth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri)
    }

    override fun resolve(request: HttpServletRequest): OAuth2AuthorizationRequest? {
        return when (request.servletPath) {
            "/oauth2/authorization/google" -> {
                var req: OAuth2AuthorizationRequest? = defaultResolver?.resolve(request)
                if (req != null)
                    req = customizeAuthorizationRequest(req, GOOGLE)
                req
            }

            else -> {
                var req: OAuth2AuthorizationRequest? = defaultResolver?.resolve(request)
                if (req != null)
                    req = customizeAuthorizationRequest(req, AUTHSCH)
                req
            }
        }
    }

    override fun resolve(request: HttpServletRequest, clientRegistrationId: String): OAuth2AuthorizationRequest? {
        var req: OAuth2AuthorizationRequest? = defaultResolver?.resolve(request, clientRegistrationId)
        if (req != null)
            req = customizeAuthorizationRequest(req, clientRegistrationId)
        return req
    }

    private fun customizeAuthorizationRequest(
        request: OAuth2AuthorizationRequest,
        clientRegistrationId: String
    ): OAuth2AuthorizationRequest? {
        return when (clientRegistrationId) {
            AUTHSCH -> OAuth2AuthorizationRequest
                .from(request)
                .scope("basic", "displayName")
                .build()


            GOOGLE -> OAuth2AuthorizationRequest
                .from(request)
                .scope("profile", "email", "openid")
                .build()

            else -> OAuth2AuthorizationRequest
                .from(request)
                .build()
        }
    }

}