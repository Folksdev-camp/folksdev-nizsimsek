package com.nizsimsek.blogApp.dto

import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class SubCommentDto @JvmOverloads constructor(

        val id: String?,
        val content: String,
        val like: Long,
        val dislike: Long,
        val createdDate: LocalDateTime,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val updatedDate: LocalDateTime? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val author: UserDto? = null,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val comment: CommentDto? = null
)
