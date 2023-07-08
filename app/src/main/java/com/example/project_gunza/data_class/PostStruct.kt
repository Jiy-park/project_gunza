package com.example.project_gunza.data_class

import android.os.Parcelable
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
    val like: Int = 0,
    val comment: List<String> = listOf(),
): Parcelable

@Parcelize
data class PostAuthorStruct(
    val userId: String = "",
    val userName: String = "",
    val userUnit: String = "",
    val userExp: Int = 0,
): Parcelable

/** * 게시글 댓글*/
data class Comment(
    val commentId: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorUnit: String = "",
    val authorLevel: Int = 0,
    val createdAt: String = "",
    val commentBody: String = "",
)






//
///** * 게시글 미리 보기 */
//@Parcelize
//data class SimplePostViewer(
//    val postId: Int,
//    val postTitle: String,
//    val postCreatorId: String,
//    val postCreatorName: String? = null,
//    val postCreatorTeam: String? = null,
//    val postCreatorLevel: Int? = null,
//    val simpleUploadDate: String,
//    val likeCount: Int = 0,
//    val commentCount: Int? = null,
//): Parcelable
//
///** * 게시글 상세 보가*/
//data class DetailPostViewer(
//    val postId: Int,
//    val postBody: String = "",
//    val detailUploadDate: String,
//    val postComments: List<Comment>
//)
//
