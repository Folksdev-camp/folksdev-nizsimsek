package com.nizsimsek.blogApp.dto

import java.time.LocalDateTime

data class CommentDto(

        val id: String,
        val content: String,
        val createdDate: LocalDateTime,
        val updatedDate: LocalDateTime,
        val user: UserDto
)
