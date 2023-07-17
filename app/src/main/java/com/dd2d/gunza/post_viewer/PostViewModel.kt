package com.dd2d.gunza.post_viewer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dd2d.gunza.data_class.*
import kotlinx.coroutines.*

/** 게시글 뷰모델*/
class PostViewModel(private val postId: String): ViewModel() {
    private val postRepository = PostRepository()

    private val _postInfo = MutableLiveData<PostStruct>()
    val postInfo: LiveData<PostStruct>
        get() = _postInfo

    private val _postAuthor = MutableLiveData<SimpleUserStruct>()
    val postAuthor: LiveData<SimpleUserStruct>
        get() = _postAuthor

    private val _commentList = MutableLiveData<Pair<List<CommentStruct>, List<SimpleUserStruct>>>()
    val commentList: LiveData<Pair<List<CommentStruct>, List<SimpleUserStruct>>>
        get() = _commentList

    init{
        Log.d("LOG_CHECK", "PostCommentViewModel :: () -> init viewModel")

        viewModelScope.launch(Dispatchers.IO) {
            postRepository.fetchPostData(postId)
            postRepository.fetchCommentData(postId)
            withContext(Dispatchers.Main){
                _postInfo.value = postRepository.post

                _postInfo.value?.let {
                    _postAuthor.value = postRepository.postAuthor
                    _commentList.value = postRepository.comment
                }
            }
        }
    }

    /** 게시글에 댓글 작성 -> 댓글 새로 가져옴*/
    fun createComment(comment: CommentStruct){
        postRepository.createComment(comment){
            viewModelScope.launch(Dispatchers.IO) {
                postRepository.fetchCommentData(postId)
                withContext(Dispatchers.Main){
                    _commentList.value = postRepository.comment

                }
            }
        }
    }

    /** 게시글의 정보를 수정
     * @see PostRepository.updatePost*/
    fun updatePost(newTitle: String, newContent: String){
        postRepository.updatePost(newTitle, newContent){
            refreshPost()
        }
    }

    /** 게시글의 좋아요 업데이트
     * @see PostRepository.updateLike*/
    fun updateLike(userId: String, isLike: Boolean, callback: () -> Unit){
        postRepository.updateLike(userId, isLike){
            refreshPost()
            callback()
        }
    }
    /** 댓글 내용 업데이트
     * @see PostRepository.updateComment*/
    fun updateComment(commentId: String, newContent: String){
        postRepository.updateComment(commentId, newContent){
            refreshComment()
        }
    }

    /** 게시글 삭제
     * @see PostRepository.deletePost*/
    fun deletePost(userId: String, postId: String, callback: () -> Unit){
        postRepository.deletePost(userId, postId){
            callback()
        }
    }

    /** 댓글 삭제
     * @see PostRepository.deleteComment*/
    fun deleteComment(commentId: String, commentAuthor: String){
        postRepository.deleteComment(commentId, commentAuthor){
            refreshComment()
        }
    }

    /** 댓글을 새로 불러옴
     * 1. 댓글 삭제
     * 2. 댓글 업데이트*/
    private fun refreshComment(){
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.fetchCommentData(postId)
            withContext(Dispatchers.Main){
                _commentList.value = postRepository.comment
            }
        }
    }
    /** 게시글을 새로 불러옴
     * 1. 게시글 내용 | 제목 업데이트
     * 2. 좋아요 업데이트*/
    private fun refreshPost(){
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.fetchPostData(postId)
            withContext(Dispatchers.Main){
                _postInfo.value = postRepository.post
            }
        }
    }
}