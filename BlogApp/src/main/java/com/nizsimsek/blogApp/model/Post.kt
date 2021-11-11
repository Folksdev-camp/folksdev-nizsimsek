package com.nizsimsek.blogApp.model

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Post @JvmOverloads constructor(

        @Id
        @GeneratedValue(generator = "UUID")
        @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
        val id: String? = "",
        val title: String,
        val content: String,
        val createdDate: LocalDateTime = LocalDateTime.now(),
        val updatedDate: LocalDateTime = LocalDateTime.now(),

        @ManyToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        val author: User,

        @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        val comments: List<Comment>,

) {
    constructor(title: String, content: String, author: User) : this(
            title = title,
            content = content,
            createdDate = LocalDateTime.now(),
            author = author,
            comments = listOf()
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Post

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(title = $title , content = $content , createdDate = $createdDate , updatedDate = $updatedDate , author = $author )"
    }
}
