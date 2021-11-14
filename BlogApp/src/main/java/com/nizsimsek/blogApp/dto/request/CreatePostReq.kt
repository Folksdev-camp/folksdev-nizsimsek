package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.*

data class CreatePostReq (

        @field:NotBlank(message = "The title value must not be empty")
        val title: String,

        @field:NotBlank(message = "The content value must not be empty")
        val content: String,

        @field:PositiveOrZero
        @field:NotNull
        val like: Int = 0,

        @field:NotBlank(message = "The authorId value must not be empty")
        val authorId: String
)