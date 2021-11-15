package com.nizsimsek.blogApp.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import javax.validation.constraints.NotBlank

data class UpdatePostReq(

        @field:NotBlank(message = "The title value must not be empty")
        val title: String,

        @field:NotBlank(message = "The content value must not be empty")
        val content: String,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val categoryIds: List<String>? = ArrayList()
)
