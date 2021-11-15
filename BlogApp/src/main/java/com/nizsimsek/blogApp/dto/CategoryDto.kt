package com.nizsimsek.blogApp.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class CategoryDto @JvmOverloads constructor(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val id: String? = null,
        val name: String,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val post: List<PostDto>? = ArrayList()

)