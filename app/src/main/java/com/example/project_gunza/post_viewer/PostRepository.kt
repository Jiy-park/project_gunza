package com.example.project_gunza.post_viewer

import android.util.Log
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.data_class.CommentStruct
import com.example.project_gunza.data_class.PostStruct
import com.example.project_gunza.data_class.SimpleUserStruct
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/** 포스트 모델*/
class PostRepository {
    private val postDB = Firebase.firestore.collection(FIELD.POST.ROOT)
    private val commentDB = Firebase.firestore.collection(FIELD.COMMENT.ROOT)
    private val userDB = Firebase.firestore.collection(FIELD.USER.ROOT)

    var post: PostStruct? = null
    var postAuthor: SimpleUserStruct? = null
    var comment: Pair<List<CommentStruct>, List<SimpleUserStruct>>? = null


    /** 댓글 작성
     * 1. post - comment 배열 업데이트
     * 2. comment 업데이트
     * 3. user - createComment 배열 업데이트*/
    fun createComment(comment: CommentStruct, callback: () -> Unit){
        postDB.document(comment.postId)
            .update(FIELD.POST.COMMENT, FieldValue.arrayUnion(comment.commentId))

        commentDB.document(comment.commentId)
            .set(comment)

        userDB.document(comment.authorId)
            .update(FIELD.USER.COMMENT, FieldValue.arrayUnion(comment.commentId))

        callback()
    }

    /** 게시글의 메인 부분 받아옴
     * 1. [post] - 게시글 메인
     * 2. [postAuthor] - 게시글 작성자 / [post] != null 일때만 */
    suspend fun fetchPostData(postId: String){
        Log.d("LOG_CHECK", "PostRepository :: fetchData() -> fetch post($postId) data")

        val value = postDB.document(postId)
            .get()
            .await()
        post = value.toObject(PostStruct::class.java)
        post?.let {
            postAuthor = getAuthor(it.authorId)
        }
    }

    /** [postId] 에 해당하는 게시글의 댓글 / 댓글 작성자 받아옴*/
    suspend fun fetchCommentData(postId: String){
        Log.d("LOG_CHECK", "PostRepository :: fetchCommentData() -> fetch comment of post($postId) data")

        val commentList = commentDB
            .whereEqualTo(FIELD.COMMENT.POST_ID, postId)
            .orderBy(FIELD.COMMENT.CREATE_AT, Query.Direction.ASCENDING)
            .get()
            .await()
            .toObjects(CommentStruct::class.java)
        val commentAuthorList = MutableList(commentList.size) { SimpleUserStruct() }

        withContext(Dispatchers.IO){
            commentList.mapIndexed { index, comment ->
                async {
                    commentAuthorList[index] = getAuthor(comment.authorId)
                }
            }.awaitAll()

            comment = Pair(commentList, commentAuthorList)
        }
    }

    private suspend fun getAuthor(authorId: String) =
        userDB.document(authorId)
            .get()
            .await()
            .toObject(SimpleUserStruct::class.java)?: SimpleUserStruct()


    /** 게시글의 내용 업데이트
     * @param newContent 게시글 내용 업데이트
     * @param newTitle 게시글 제목 업데이트*/
    fun updatePost(newTitle: String, newContent: String, callback: () -> Unit){
        val ref = postDB.document(post!!.postId)

        if(post?.postTitle != newTitle || post?.content != newContent) {
            ref.update(FIELD.POST.TITLE, newTitle)
            ref.update(FIELD.POST.CONTENT, newContent)
            callback()
        }
    }

    /** 게시글의 좋아요 업데이트
     * @param isLike true -> 좋아요
     * 1. 게시글 좋아요 업데이트
     * 2. 유저 좋아요 업데이트*/
    fun updateLike(userId: String, isLike: Boolean, callback: () -> Unit){
        postDB.document(post!!.postId)
            .update(
                FIELD.POST.LIKE, FieldValue.increment(
                if(isLike) { 1L } // 좋아요 시 1증가
                else { -1L } // 아닐 시 1 감소
            ))

        userDB.document(userId)
            .update(
                FIELD.USER.LIKE_POST,
                if(isLike) { FieldValue.arrayUnion(post!!.postId) }
                else { FieldValue.arrayRemove(post!!.postId) }
            )

        callback()
    }

    /** 게시글 삭제
     * 1. post 에서 [postId] 에 해당하는 document 삭제
     * 2. user - createPost 에서 [postId]에 해당하는 요소 삭제
     * 3. comment 에서 postId == [postId] 안 document 삭제*/
    fun deletePost(userId: String, postId: String, callback: () -> Unit){
        postDB.document(postId).delete()
        userDB.document(userId).update(FIELD.USER.POST, FieldValue.arrayRemove(postId))
        callback()
        commentDB.whereEqualTo(FIELD.COMMENT.POST_ID, postId).get().addOnSuccessListener { docs ->
            for(doc in docs){ doc.reference.delete() }
        }
    }

    /** 댓글 수정 기존 댓글 내용과 차이점이 없으면 업데이트 실행 안함 */
    fun updateComment(commentId: String, newContent: String, callback: () -> Unit){
        val before = comment?.first?.find {
             it.commentId == commentId
        }

        before?.let {
            if(it.commentBody != newContent){
                commentDB
                    .document(commentId).update(FIELD.COMMENT.CONTENT, newContent)
                    .addOnSuccessListener {
                    callback()
                }
            }
        }
    }

    /** 댓글 삭제
     * 1. post - comment 에서 [commentId] 에 해당하는 요소 삭제
     * 2. user - createComment 에서 [commentId] 에 해당하는 요소 삭제
     * 3. comment 에서 [commentId] 에 해당하는 document 삭제*/
    fun deleteComment(commentId: String, commentAuthor: String, callback: () -> Unit){
        postDB.document(post!!.postId).update(FIELD.POST.COMMENT, FieldValue.arrayRemove(commentId))
            .addOnSuccessListener { callback() }

        userDB.document(commentAuthor).update(FIELD.USER.COMMENT, FieldValue.arrayRemove(commentId))
        commentDB.document(commentId).delete()
    }
}