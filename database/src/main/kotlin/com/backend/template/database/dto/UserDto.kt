package com.backend.template.database.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime
import java.util.*

data class UserDto(
    val id: Int,
    val publicId: UUID,
    val created: LocalDateTime,
    @field: NotNull val password: String,
    @field: Email val email: String,
    @field: NotNull val username: String,
    val role: RoleEnum
) {
}