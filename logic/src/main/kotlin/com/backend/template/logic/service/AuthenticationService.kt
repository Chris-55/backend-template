package com.backend.template.logic.service

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.TokenExpiredException
import com.auth0.jwt.interfaces.DecodedJWT
import com.backend.template.database.dao.UserDao
import com.backend.template.database.dto.LoginDto
import com.backend.template.database.dto.TokenDto
import com.backend.template.database.dto.UserDto
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.util.*

@Service
open class AuthenticationService (
    private val passwordEncoder: PasswordEncoder,
    private val userDao: UserDao,
    @Value("\${jwt.secret}") private val jwtSecret: String
) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    private val algorithm: Algorithm = Algorithm.HMAC256(jwtSecret)
    private val verifier: JWTVerifier = JWT.require(algorithm)
        .withIssuer(JWT_ISSUER)
        .build()

    open fun authenticate(loginDto: LoginDto): TokenDto? {
        val user: UserDto = userDao.getUserByUsername(loginDto.username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")

        if (passwordEncoder.matches(loginDto.password, user.password)) {
            logger.info("User ${user.publicId} authenticated")
            return createJwt(user)
        }

        logger.info("Authentication for user ${user.username} failed")
        return null
    }

    open fun verifyJwt(token: String): DecodedJWT {
        try {
            val jwt = verifier.verify(token)
            logger.info("JWT verification successful")
            return jwt
        } catch (e: TokenExpiredException) {
            logger.info("JWT verification failed: token expired")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired")
        } catch (e: Exception) {
            logger.info("JWT verification failed: ${e.message}")
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")
        }
    }

    private fun createJwt(user: UserDto): TokenDto {
        try {
            val timestamp: Long = System.currentTimeMillis() // current time in milliseconds
            val expiration: Long = timestamp + 1000 * 60 * 60 * 24 * 7 // 7 days

            val token = JWT.create()
                .withIssuer(JWT_ISSUER)
                .withClaim("id", user.publicId.toString())
                .withClaim("role", user.role.toString())
                .withExpiresAt(Date(expiration))
                .sign(algorithm)

            return TokenDto(token, expiration)
        } catch (e: Exception) {
            logger.error("Error creating JWT: ${e.message}")
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating JWT")
        }
    }

    companion object {
        const val JWT_ISSUER = "custom issuer"
    }
}