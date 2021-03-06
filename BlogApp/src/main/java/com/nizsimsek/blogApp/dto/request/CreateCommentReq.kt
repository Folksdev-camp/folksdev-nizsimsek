package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.NotBlank

data class CreateCommentReq (

        @field:NotBlank(message = "The content value must not be empty")
        val content: String,

        @field:NotBlank(message = "The authorId value must not be empty")
        val authorId: String,

        @field:NotBlank(message = "The postId value must not be empty")
        val postId: String
)