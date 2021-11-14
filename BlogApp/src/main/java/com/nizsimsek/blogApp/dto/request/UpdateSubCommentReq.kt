package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.NotBlank

data class UpdateSubCommentReq(

        @field:NotBlank(message = "The content value must not be empty")
        val content: String
)
