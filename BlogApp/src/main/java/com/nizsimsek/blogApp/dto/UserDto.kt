package com.nizsimsek.blogApp.dto

import com.fasterxml.jackson.annotation.JsonInclude

data class UserDto @JvmOverloads constructor(

        val id: String?,
        val username: String,
        val email: String,
        val firstName: String,
        val lastName: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val password: String? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val posts: List<PostDto>? = ArrayList(),

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val comments: List<CommentDto>? = ArrayList(),

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val subComments: List<SubCommentDto>? = ArrayList()
)