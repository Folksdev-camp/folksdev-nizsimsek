package com.nizsimsek.blogApp.model

import org.hibernate.Hibernate
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Max

@Entity
data class Comment @JvmOverloads constructor(

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

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        val author: User,

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id", referencedColumnName = "id")
        val post: Post,

        @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        val subComments: List<SubComment>? = ArrayList()

) {
        override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
                other as Comment

                return id != null && id == other.id
        }

        override fun hashCode(): Int = javaClass.hashCode()

        @Override
        override fun toString(): String {
                return this::class.simpleName + "(content = $content , likes = $likes , dislikes = $dislikes , createdDate = $createdDate , updatedDate = $updatedDate )"
        }
}