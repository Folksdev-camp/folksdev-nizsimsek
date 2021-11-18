package com.nizsimsek.blogApp.dto.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class CreateUserReq (

        @field:NotBlank(message = "The username value must not be empty")
        val username: String,

        @field:NotBlank(message = "The email value must not be empty")
        @field:Email
        val email: String,

        @field:NotBlank(message = "The firstname value must not be empty")
        val firstName: String,

        @field:NotBlank(message = "The lastname value must not be empty")
        val lastName: String,

        @field:NotBlank(message = "The password value must not be empty")
        val password: String
)