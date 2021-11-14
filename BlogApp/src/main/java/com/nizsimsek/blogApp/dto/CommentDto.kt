package com.nizsimsek.blogApp.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class CommentDto @JvmOverloads constructor(

        val id: String?,
        val content: String,
        val like: Int,
        val createdDate: LocalDateTime,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val updatedDate: LocalDateTime? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val author: UserDto? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val post: PostDto? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val subComment: List<SubCommentDto>? = ArrayList()
)