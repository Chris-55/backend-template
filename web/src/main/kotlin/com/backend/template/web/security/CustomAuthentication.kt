package com.backend.template.web.security

import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CustomAuthentication (
    private val jwt: DecodedJWT
) : AbstractAuthenticationToken(null) {

    init {
        isAuthenticated = true
    }

    override fun getCredentials(): DecodedJWT {
        return jwt
    }

    override fun getPrincipal(): String {
        return jwt.getClaim("id").asString()
    }

    override fun getAuthorities(): MutableCollection<GrantedAuthority> {
        val role = jwt.getClaim("role").asString()
        val authorities = mutableListOf<GrantedAuthority>()
        authorities.add(GrantedAuthority { role })
        return authorities
    }
}