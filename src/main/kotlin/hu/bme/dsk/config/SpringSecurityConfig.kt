package hu.bme.dsk.config

import hu.bme.dsk.jwt.JwtConfigurer
import hu.bme.dsk.jwt.JwtTokenService
import hu.bme.dsk.login.LoginRejectedException
import hu.bme.dsk.login.LoginService
import hu.bme.dsk.login.authsch.LoginAuthschUser
import hu.bme.dsk.login.authsch.AuthschProfileResponse
import hu.bme.dsk.login.google.GoogleUserInfoResponse
import hu.bme.dsk.login.google.LoginGoogleUser
import hu.bme.dsk.users.UserRole
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.reactive.function.client.WebClient
import tools.jackson.databind.ObjectMapper
import org.springframework.security.config.Customizer
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.web.reactive.function.client.bodyToMono
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlin.jvm.java


@Configuration
class SpringSecurityConfig(
    private val clientRegistrationRepository: ClientRegistrationRepository,
    private val objectMapper: ObjectMapper,
    private val jwtTokenService: JwtTokenService,
//    private val loginComponent: LoginComponent,
    private val startupPropertyConfig: StartupPropertyConfig,
//    private val auditLogService: AuditLogService,
    private val loginService: LoginService,
//    private val userDetailsService: LoginUserDetailsService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration): AuthenticationManager {
        return config.authenticationManager
    }

    var authschUserServiceClient = WebClient.builder()
        .baseUrl("https://auth.sch.bme.hu/api")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, "AuthSchKotlinAPI")
        .build()

    var googleUserServiceClient = WebClient.builder()
        .baseUrl("https://www.googleapis.com/oauth2/v3")
        .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        .defaultHeader(HttpHeaders.USER_AGENT, "AuthSchKotlinAPI")
        .build()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests {
            it.requestMatchers(
                "/",
                "/error",
                "/403",
                "/404",
                "/control/loggedin",
                "/control/login",
                "/control/post-login",
                "/style.css",
                "/flatpickr_custom.css",
                "/tabulator_simple.css",
                "/tabulator_simple.min.css.map",
                "/control/test-user",
                "/images/**",
                "/js/**",
                "/docs-icons/**",
                "/files/**",
                "/admin/logout",
                "/countdown",
                "/control/logout",
                "/control/test",
                "/control/open-site",
                "/api/**",
                "/remote-api/**",
                "/share/**",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/manifest/manifest.json",
                "/control/refresh",
                "/oauth2/authorization",
                "/c/**",
                "/ol.js",
                "/ol.css",
                "/tracker.css",
                "/scanner.css",
                "/coloris.min.css",
                "/coloris.min.js",
            ).permitAll()
            it.requestMatchers("/admin/**").hasAnyRole(
                UserRole.ADMIN.name
            )
            it.requestMatchers("/admin/blog/**").hasAnyRole(
                UserRole.BLOGGER.name
            )
        }
        http.formLogin { it.disable() }
        http.exceptionHandling { it.accessDeniedPage("/403") }
        http.with(JwtConfigurer(jwtTokenService), Customizer.withDefaults())
//        http.with(SessionFilterConfigurer(startupPropertyConfig), Customizer.withDefaults())

        http.oauth2Login { oauth2 ->
            oauth2.loginPage("/oauth2/authorization")
                .authorizationEndpoint {
                    it.authorizationRequestResolver(
                        CustomAuthorizationRequestResolver(
                            clientRegistrationRepository, "/oauth2/authorization"
                        )
                    )
                }.userInfoEndpoint { userInfo ->
                    userInfo
                        .oidcUserService {
                            resolveGoogleUser(it)
                        }
                        .userService { resolveAuthschUser(it) }
                }.defaultSuccessUrl("/")
                .failureHandler { request, response, exception ->
                    val message = if (exception is LoginRejectedException)
                        exception.userMessage
                    else
                        "Sikertelen bejelentkezés."
                    log.info("OAuth2 login failed: ${exception.message}")
                    val encoded = URLEncoder.encode(message, StandardCharsets.UTF_8)
                    response.sendRedirect("/oauth2/authorization?error=$encoded")
                }
        }



        http.csrf {
            it.ignoringRequestMatchers(
                "/api/**",
                "/admin/api/**",
            )
        }.cors(Customizer.withDefaults())
        return http.build()
    }

    private fun resolveAuthschUser(request: OAuth2UserRequest): DefaultOAuth2User {
        // The API returns `test/json` which is an invalid mime type
        val authschProfileJson: String? = authschUserServiceClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/profile/")
                    .queryParam("access_token", request.accessToken.tokenValue)
                    .build()
            }
            .retrieve()
            .bodyToMono<String>()
            .block()

        val profile = objectMapper.readerFor(AuthschProfileResponse::class.java)
            .readValue<AuthschProfileResponse>(authschProfileJson)!!
        val userEntity = loginService.fetchAuthschUserEntity(profile)

//        auditLogService.login(userEntity, "authsch user login g:${userEntity.group} r:${userEntity.role}")

        return LoginAuthschUser(
            id = userEntity.id,
            authId = userEntity.authId,
            googleId = null,
            roles = userEntity.roles,
            userName = userEntity.username,
            authorities = userEntity.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") },
        )
    }

    private fun resolveGoogleUser(request: OidcUserRequest): DefaultOidcUser {
        val googleProfileJson: String? = googleUserServiceClient.get()
            .uri { uriBuilder ->
                uriBuilder.path("/userinfo")
                    .build()
            }
            .header("Authorization", "Bearer " + request.accessToken.tokenValue)
            .retrieve()
            .bodyToMono<String>()
            .block()

        val profile = objectMapper.readerFor(GoogleUserInfoResponse::class.java)
            .readValue<GoogleUserInfoResponse>(googleProfileJson)!!
        log.info("google profile response = $profile")
        val userEntity = loginService.fetchGoogleUserEntity(profile)

//        auditLogService.login(userEntity, "google user login g:${userEntity.group} r:${userEntity.role}")

        return LoginGoogleUser(
            id = userEntity.id,
            authId = null,
            googleId = userEntity.googleId,
            roles = userEntity.roles,
            userName = userEntity.username,
            authorities = userEntity.roles.map { SimpleGrantedAuthority("ROLE_${it.name}") },
            idToken = request.idToken,
        )
    }

}