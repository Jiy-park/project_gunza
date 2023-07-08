package com.example.project_gunza.post_viewer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.data_class.Comment
import com.example.project_gunza.data_class.PostStruct
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

/** 포스트 모델*/
class PostRepository {
    private val postDB = Firebase.firestore.collection(FIELD.POST.ROOT)
    private val commentDB = Firebase.firestore.collection(FIELD.COMMENT.ROOT)

    var post: PostStruct? = null
    var comment = listOf<Comment>()

    private fun fetchPostData(postId: String){
        Log.d("LOG_CHECK", "PostRepository :: fetchData() -> fetch post($postId) data")

        postDB.document(postId)
            .get()
            .addOnSuccessListener {
                post = it.toObject(PostStruct::class.java)
                fetchCommentData(postId)
            }
    }

    private fun fetchCommentData(postId: String){
        Log.d("LOG_CHECK", "PostRepository :: fetchCommentData() -> fetch comment of post($postId) data")

        commentDB.whereEqualTo(FIELD.COMMENT.POST_ID, postId)
            .orderBy(FIELD.COMMENT.CREATE_AT, Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                comment =
            }
    }
}


/** 포스트 뷰모델*/
class PostViewModel(private val postId: String): ViewModel() {
    private val db = Firebase.firestore

    private val _postInfo = MutableLiveData<List<Comment>>()

    val postInfo: LiveData<List<Comment>>
        get() = _postInfo

    init{
        Log.d("LOG_CHECK", "PostCommentViewModel :: () -> init viewModel")
        db.collection(FIELD.COMMENT.ROOT)
            .whereEqualTo(FIELD.POST.ID, postId)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e("LOG_CHECK", "PostCommentViewModel :: error in link to comment db () -> ${it.message}")
                }?: run{
                    _postInfo.value = value?.toList()?.map { it.toObject(Comment::class.java) }
                }
            }
    }

    fun createComment(comment: Comment, callback: ()->Unit){
        db.collection(FIELD.COMMENT.ROOT)
            .document(comment.commentId)
            .set(comment)
            .addOnSuccessListener {
                callback()
            }
    }

    /** 유저가 댓글을 작성했을 경우 관련 데이터베이스 업데이트
     *- user - createComment
     *- post - comment */
    fun updateByCreateComment(userId: String, postId: String, commentId: String){
        Log.d("LOG_CHECK", "PostCommentViewModel :: updateByCreateComment() -> " +
                "userId : $userId\n" +
                "postId : $postId\n" +
                "commentId : $commentId\n")
        // user 업데이트
        db.collection(FIELD.USER.ROOT)
            .document(userId)
            .update(FIELD.USER.COMMENT, FieldValue.arrayUnion(commentId))

        // post 업데이트
        db.collection(FIELD.POST.ROOT)
            .document(postId)
            .update(FIELD.POST.COMMENT, FieldValue.arrayUnion(commentId))
    }

    /** 포스터의 좋아요 버튼 클릭*/
    fun likePost(postId: String, callback: ()->Unit){
        db.collection(FIELD.POST.ROOT)
            .document(postId)
            .update(FIELD.POST.LIKE, FieldValue.increment(1L))
            .addOnSuccessListener {
                callback()
            }
    }
}