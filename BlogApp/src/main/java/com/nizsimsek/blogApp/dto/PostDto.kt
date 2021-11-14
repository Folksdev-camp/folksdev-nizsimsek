package com.nizsimsek.blogApp.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class PostDto @JvmOverloads constructor(

        val id: String?,
        val title: String,
        val content: String,
        val like: Int,
        val createdDate: LocalDateTime,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val updatedDate: LocalDateTime? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val author: UserDto? = null,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val comments: List<CommentDto>? = ArrayList(),

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val subComments: List<SubCommentDto>? = ArrayList()
)
