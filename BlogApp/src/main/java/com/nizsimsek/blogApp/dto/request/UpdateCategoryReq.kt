package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.NotBlank

data class UpdateCategoryReq (

        @field:NotBlank(message = "The name value must not be empty")
        val name: String
)
