package hu.bme.dsk.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository
import org.springframework.security.oauth2.core.AuthenticationMethod
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod

@Configuration
class AuthConfig(
    @param:Value("\${spring.security.oauth2.client.registration.authsch.client-id:}") private val authschId: String,
    @param:Value("\${spring.security.oauth2.client.registration.authsch.client-secret:}") private val authschSecret: String,
    @param:Value("\${spring.security.oauth2.client.registration.authsch.redirect-uri:}") private val authschRedirectUrl: String,
    @param:Value("\${spring.security.oauth2.client.provider.authsch.authorization-uri:}") private val authschAuthorizationUrl: String,
    @param:Value("\${spring.security.oauth2.client.provider.authsch.token-uri:}") private val authschTokenUri: String,
    @param:Value("\${spring.security.oauth2.client.provider.authsch.user-info-uri:https://auth.sch.bme.hu/oidc/userinfo}") private val userInfoUri: String,

    @param:Value("\${spring.security.oauth2.client.registration.google.client-id:}") private val googleId: String,
    @param:Value("\${spring.security.oauth2.client.registration.google.client-secret:}") private val googleSecret: String,
    @param:Value("\${spring.security.oauth2.client.registration.google.redirect-uri:}") private val googleRedirectUrl: String,
    @param:Value("\${spring.security.oauth2.client.provider.google.authorization-uri:https://accounts.google.com/o/oauth2/auth}") private val googleAuthorizationUrl: String,
    @param:Value("\${spring.security.oauth2.client.provider.google.token-uri:https://accounts.google.com/o/oauth2/token}") private val googleTokenUri: String,
    ) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun clientRegistrationRepository(): ClientRegistrationRepository {
        val authProviders = mutableListOf<ClientRegistration>()
        log.info("Using oauth2 sso: authsch")
        authProviders.add(authschClientRegistration())
        log.info("scopes: {}", authProviders.first().scopes)

        if (googleId.isNotBlank() && googleId != "no") {
            log.info("Using oauth2 sso: google")
            authProviders.add(googleClientRegistration())
        }

        return InMemoryClientRegistrationRepository(authProviders)
    }

    private fun authschClientRegistration(): ClientRegistration {
        return ClientRegistration.withRegistrationId("authsch")
            .clientId(authschId)
            .clientSecret(authschSecret)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .redirectUri(authschRedirectUrl)
            .scope("basic", "displayName", "sn", "givenName", "mail", "openid")
            .authorizationUri(authschAuthorizationUrl)
            .tokenUri(authschTokenUri)
            .userInfoUri(userInfoUri)
            .userInfoAuthenticationMethod(AuthenticationMethod.QUERY)
            .userNameAttributeName("internal_id")
            .clientName("AuthSch")
            .build()
    }

    private fun googleClientRegistration(): ClientRegistration {
        return ClientRegistration.withRegistrationId("google")
            .clientId(googleId)
            .clientSecret(googleSecret)
            .redirectUri(googleRedirectUrl)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .scope("profile", "email", "openid")
            .authorizationUri(googleAuthorizationUrl)
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
            .issuerUri("https://accounts.google.com")
            .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
            .tokenUri(googleTokenUri)
            .clientName("Google")
            .build()
    }
}