package com.example.project_gunza.data_class

import android.os.Parcelable
import com.example.project_gunza.common.VALUE
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostStruct(
    val postId: String = "",
    val groupId: String = "",
    val authorId: String = "",
    val postTitle: String = "",
    val createdAt: String = "",
    val content: String = "",
    val like: Int = 0,
    val comment: List<String> = listOf(),
): Parcelable

@Parcelize
data class SimplePostStruct(
    val postId: String = "",
    val authorId: String = "",
    val postTitle: String = "",
    val createdAt: String = "",
    val like: Int = 0,
    val comment: List<String> = listOf(),
): Parcelable

data class CommentStruct(
    val commentId: String = "",
    val postId: String = "",
    val authorId: String = "",
    val createdAt: String = "",
    val commentBody: String = "",
)