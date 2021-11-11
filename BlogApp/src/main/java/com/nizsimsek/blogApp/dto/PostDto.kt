package com.nizsimsek.blogApp.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class PostDto(

        val id: String,
        val title: String,
        val content: String,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val updatedDate: LocalDateTime = LocalDateTime.now(),
        val author: UserDto,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val comments: List<CommentDto>? = ArrayList()
)
