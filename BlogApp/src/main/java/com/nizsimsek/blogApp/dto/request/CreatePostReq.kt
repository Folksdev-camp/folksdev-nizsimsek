package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.NotBlank

data class CreatePostReq (

        @field:NotBlank(message = "The title value must not be empty")
        val title: String,

        @field:NotBlank(message = "The content value must not be empty")
        val content: String,

        @field:NotBlank(message = "The authorId value must not be empty")
        val authorId: String,

        @field:NotBlank(message = "The categoryIds value must not be empty")
        val categoryIds: List<String>
)