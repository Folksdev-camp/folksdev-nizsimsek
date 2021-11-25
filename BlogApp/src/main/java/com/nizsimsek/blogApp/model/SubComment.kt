package com.nizsimsek.blogApp.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Max

@Entity
data class SubComment @JvmOverloads constructor(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        val id: String? = "",

        @Column(length = 5000)
        val content: String,

        @Max(Long.MAX_VALUE)
        val likes: Long = 0,

        @Max(Long.MAX_VALUE)
        val dislikes: Long = 0,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val updatedDate: LocalDateTime = LocalDateTime.now(),

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        @JsonIgnore
        val author: User,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "comment_id", referencedColumnName = "id")
        @JsonIgnore
        val comment: Comment

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as SubComment

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(content = $content , likes = $likes , dislikes = $dislikes , createdDate = $createdDate , updatedDate = $updatedDate )"
    }
}