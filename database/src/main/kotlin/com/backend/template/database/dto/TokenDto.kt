package com.backend.template.database.dto

data class TokenDto(
    val token: String,
    val expiration: Long,
)