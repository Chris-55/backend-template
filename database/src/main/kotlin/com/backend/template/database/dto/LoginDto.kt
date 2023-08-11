package com.backend.template.database.dto

import jakarta.validation.constraints.NotNull


data class LoginDto(
    @field: NotNull val username: String?,
    @field: NotNull val password: String?
)