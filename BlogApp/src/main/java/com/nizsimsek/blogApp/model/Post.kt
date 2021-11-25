package com.nizsimsek.blogApp.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Max

@Entity
data class Post @JvmOverloads constructor(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        val id: String? = "",
        val title: String,

        val content: String,

        @Max(Long.MAX_VALUE)
        val likes: Long = 0,

        @Max(Long.MAX_VALUE)
        val dislikes: Long = 0,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val updatedDate: LocalDateTime? = LocalDateTime.now(),

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(
                name = "post_categories",
                joinColumns = [JoinColumn(name = "post_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "category_id", referencedColumnName = "id")]
        )
        val category: List<Category>,

        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        @JsonIgnore
        val author: User,

        @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        val comments: List<Comment>? = ArrayList()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Post

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(title = $title , content = $content , likes = $likes , dislikes = $dislikes , createdDate = $createdDate , updatedDate = $updatedDate )"
    }
}