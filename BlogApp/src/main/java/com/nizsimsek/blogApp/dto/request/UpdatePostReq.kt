package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.NotBlank

data class UpdatePostReq(

        @field:NotBlank(message = "The title value must not be empty")
        val title: String,

        @field:NotBlank(message = "The content value must not be empty")
        val content: String
)
