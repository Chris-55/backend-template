package com.backend.template.web.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
open class SecurityConfig(
    private val filter: CustomAuthenticationFilter
) {

    private val unauthenticatedEndpoints: Array<String> = arrayOf(
        "/info/**",
        "/v1/authentication/**"
    )

    @Bean
    open fun configure(http: HttpSecurity): SecurityFilterChain? {
        http.httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
            .authorizeHttpRequests().requestMatchers(*unauthenticatedEndpoints).permitAll()
            .anyRequest().authenticated().and()
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)
            .cors(Customizer.withDefaults())

        return http.build()
    }

    @Bean
    open fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
