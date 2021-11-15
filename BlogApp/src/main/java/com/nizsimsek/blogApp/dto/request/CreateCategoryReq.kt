package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.NotBlank

data class CreateCategoryReq (

        @field:NotBlank(message = "The name value must not be empty")
        val name: String
)
