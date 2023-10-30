package com.mvm.remindme.service

import com.mvm.remindme.error.BadRequestException
import com.mvm.remindme.repository.model.User
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TokenService(
    private val jwtDecoder: JwtDecoder,
    private val jwtCustomEncoder: JwtEncoder,
    private val userService: UserService,
) {
    fun createToken(user: User): String {
        val jwsHeader = JwsHeader.with { "HS256" }.build()
        val claims = JwtClaimsSet.builder()
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plus(10L, ChronoUnit.MINUTES))
            .subject(user.userName)
            .claim("userName", user.userName)
            .build()
        return jwtCustomEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).tokenValue
    }

    fun parseToken(token: String): User? {
        return try {
            val jwt = jwtDecoder.decode(token)
            val userId = jwt.claims["userName"] as String
            userService.findById(userId)
        } catch (e: Exception) {
            null
        }
    }

    fun checkPassword(input: String, hash: String): Boolean {
        try {
            return BCrypt.checkpw(input, hash)
        } catch (exception: Exception) {
            throw BadRequestException.PasswordNotMatchingException("Password not matching")
        }

    }

    fun hashBcrypt(input: String): String {
        return BCrypt.hashpw(input, BCrypt.gensalt(10))
    }
}