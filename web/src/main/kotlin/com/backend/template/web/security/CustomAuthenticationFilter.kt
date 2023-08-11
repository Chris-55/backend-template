package com.backend.template.web.security

import com.backend.template.logic.service.AuthenticationService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class CustomAuthenticationFilter(
    private val authenticationService: AuthenticationService,
    private val provider: CustomAuthenticationProvider
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION) ?: ""

        if (!authenticationHeader.startsWith("Bearer ", ignoreCase = true)) {
            return filterChain.doFilter(request, response)
        }

        try {
            val jwtToken = authenticationService.verifyJwt(authenticationHeader.substring(7))
            val authentication = CustomAuthentication(jwtToken)
            val authenticated = provider.authenticate(authentication)
            SecurityContextHolder.getContext().authentication = authenticated
        } catch (e: Exception) {
            SecurityContextHolder.clearContext()
        }
    }
}
