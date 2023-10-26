package com.mvm.remindme.config

import com.mvm.remindme.service.TokenService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
@EnableWebSecurity
class SecurityConfig(private val tokenService: TokenService) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            authorizeRequests {
                authorize(HttpMethod.POST, "/auth/login", permitAll)
                authorize(HttpMethod.POST, "/auth/register", permitAll)
                authorize("/swagger-ui/**", permitAll)
                authorize("/swagger-ui.html", permitAll)
                authorize("/v3/api-docs/**", permitAll)
                authorize("/webjars/**", permitAll)
                authorize("/swagger-resources/**", permitAll)
                authorize("/api/**", authenticated)
                authorize(anyRequest, permitAll)
            }
            oauth2ResourceServer {
                jwt {}
            }
            http.authenticationManager { auth ->
                val jwt = auth as BearerTokenAuthenticationToken
                val user = tokenService.parseToken(jwt.token) ?: throw InvalidBearerTokenException("Invalid token")
                UsernamePasswordAuthenticationToken(user, "", listOf(SimpleGrantedAuthority("USER")))
            }
           http.csrf { csrf -> csrf.disable() }
           http.cors {  }
        }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()

        // Allow requests from a specific origin
        config.addAllowedOrigin("http://localhost:4200")

        // Allow specific HTTP methods
        config.addAllowedMethod("GET")
        config.addAllowedMethod("POST")
        config.addAllowedMethod("PUT")
        config.addAllowedMethod("DELETE")

        // Allow specific headers
        config.addAllowedHeader("Authorization")
        config.addAllowedHeader("Content-Type")
        source.registerCorsConfiguration("/**", config)
        return source
    }

}
