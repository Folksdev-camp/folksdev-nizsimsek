package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.*

data class CreateSubCommentReq (

        @field:NotBlank(message = "The content value must not be empty")
        val content: String,

        @field:NotBlank(message = "The authorId value must not be empty")
        val authorId: String,

        @field:NotBlank(message = "The commentId value must not be empty")
        val commentId: String
)