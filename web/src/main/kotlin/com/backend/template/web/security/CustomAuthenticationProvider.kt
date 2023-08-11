package com.backend.template.web.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationProvider : AuthenticationProvider {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun authenticate(authentication: Authentication?): Authentication? {
        if (authentication !is CustomAuthentication) {
            logger.info("Authentication is not instance of CustomAuthentication")
        }

        return authentication
    }

    override fun supports(authentication: Class<*>): Boolean {
        return CustomAuthentication::class.java.isAssignableFrom(authentication)
    }
}
