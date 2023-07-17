package com.dd2d.gunza.post_viewer

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dd2d.gunza.*
import com.dd2d.gunza.common.*
import com.dd2d.gunza.data_class.CommentStruct
import com.dd2d.gunza.databinding.ActivityStudyGroupPostViewerBinding
import com.dd2d.gunza.dialog.DialogFunc
import com.dd2d.gunza.main_page.UserViewModel
import java.text.SimpleDateFormat

@Suppress("UNUSED_PARAMETER")
class StudyGroupPost : AppCompatActivity() {
    private lateinit var binding: ActivityStudyGroupPostViewerBinding
    private lateinit var context: Context
    private lateinit var pref: Preference

    private lateinit var postViewModel: PostViewModel
    private lateinit var userViewModel: UserViewModel

    private var postId = ""
    private var isUserLikePost = false
    private var modifyMode = false

    private lateinit var commentAdapter: PostCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        getDataFromIntent()
        setViewModel()
        setView()

        binding.recyclerviewComment.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = commentAdapter
        }
    }

    private fun init(){
        binding = DataBindingUtil.setContentView(this@StudyGroupPost, R.layout.activity_study_group_post_viewer)
        context = binding.root.context
        pref = Preference(context)
        commentAdapter = PostCommentAdapter(update = { commentId, newContent ->
            modifyComment(commentId, newContent)
        }, delete = { commentId ->
            deleteComment(commentId)
        })
    }

    private fun setView(){
        isUserLikePost = checkLike()

        binding.activity = this@StudyGroupPost
        binding.likePost = isUserLikePost
        binding.modifyMode = modifyMode
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel(){
        userViewModel = ViewModelProvider(this@StudyGroupPost, ViewModelFactory(pref.getUserId()))[UserViewModel::class.java]

        postViewModel = ViewModelProvider(this@StudyGroupPost, ViewModelFactory(postId))[PostViewModel::class.java]
        postViewModel.postInfo.observe(this@StudyGroupPost){ postInfo ->
            postInfo?.let {
                binding.post = postInfo
                binding.isAuthor = checkAuthor(postInfo.authorId)
            }?: run { postNotFound() }
        }

        postViewModel.postAuthor.observe(this@StudyGroupPost){ postAuthor ->
            binding.author = postAuthor
        }

        postViewModel.commentList.observe(this@StudyGroupPost){ comment ->
            binding.commentCount = comment.first.size
            commentAdapter.commentList = comment.first
            commentAdapter.authorList = comment.second
            commentAdapter.notifyDataSetChanged()
        }
    }

    /** * 인텐트로 넘어온 데이터를 받아옴*/
    private fun getDataFromIntent(){
        postId = intent.getStringExtra(INTENT.POST.ID)?: EXCEPTION.NOT_FOUND_GROUP_ID
        if(postId.isEmpty() || postId == EXCEPTION.NOT_FOUND_POST_ID){
            postNotFound()
        }
    }

    /** 게시글 제목, 내용을 수정함*
     * @see completeModify*/
    fun modifyPost(v: View){
        modifyMode = !modifyMode
        binding.modifyMode = modifyMode
        binding.editPostContent.requestFocus()
    }

    /** 게시글 업데이트 완료
     * @see PostViewModel.updatePost*/
    fun completeModify(v: View){
        val newTitle = binding.editPostTitle.text.toString()
        val newContent = binding.editPostContent.text.toString()

        if(newTitle.isEmpty() || newContent.isEmpty()) {
            Toast.makeText(context, "제목과 내용은 입력해야 합니다.", Toast.LENGTH_SHORT).show()
        }else{
            modifyMode = !modifyMode
            binding.modifyMode = modifyMode
            postViewModel.updatePost(newTitle, newContent)
        }
    }

    /** 게시글 수정 취소*/
    fun cancelModify(v: View){
        modifyMode = !modifyMode
        binding.modifyMode = modifyMode
    }

    /** 게시글 삭제*/
    fun deletePost(v: View){
        DialogFunc.deletePostInfoDialog(context){
            postViewModel.deletePost(pref.getUserId(), postId){
                Toast.makeText(context, "게시글이 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    /** 유저가 좋아요 버튼을 눌렀었는지 체크
     * @return true-> 좋아요 눌렀음 */
    private fun checkLike(): Boolean{
        val userLikeList = userViewModel.userInfo.value?.userLikePost?: emptyList()
        return userLikeList.contains(postId)
    }

    /** 게시글의 작성자 판단
     * @return 작성자임*/
    private fun checkAuthor(authorId: String) = authorId == pref.getUserId()

    /** 게시글 좋아요 버튼
     * 1. 게시글의 좋아요 업데이트
     * 2. 유저의 좋아요 업데이트
     * 3. 뷰 업데이트
     * @see PostViewModel.updateLike*/
    fun likePost(v: View){
        isUserLikePost = !isUserLikePost

        postViewModel.updateLike(pref.getUserId(), isUserLikePost){
            binding.likePost = isUserLikePost
        }
    }

    /** 게시글에 댓글 남김
     * @see PostViewModel.createComment*/
    @SuppressLint("SimpleDateFormat")
    fun createComment(v: View){
        val content = binding.editCommentContent.text.toString()
        if(content.isEmpty()) { binding.editCommentContent.hint = "내용을 입력해야 합니다." }
        else{
            val currentTime = SimpleDateFormat("yyyy.MM.dd  HH:mm:ss").format(System.currentTimeMillis())
            val userId = pref.getUserId()
            val comment = CommentStruct(
                commentId = userId + postId + currentTime,
                postId = postId,
                authorId = userId,
                createdAt = currentTime,
                commentBody = content,
            )

            postViewModel.createComment(comment)
            binding.editCommentContent.text.clear()
        }
    }

    /** * 포스트를 찾지 못한 경우 호출*/
    private fun postNotFound() {
        Toast.makeText(context, "해당 게시글은 삭제 되었습니다.", Toast.LENGTH_SHORT).show()
        finish()
    }

    /** 댓글 수정
     * @see PostCommentAdapter.Holder.modifyComment - 해당 함수를 호출
     * @see PostViewModel.updateComment*/
    private fun modifyComment(commentId: String, newContent: String){
        postViewModel.updateComment(commentId, newContent)
    }

    /** 댓글 삭제
     * @see PostCommentAdapter.Holder.deleteComment - 해당 함수를 호출
     * @see PostViewModel.deleteComment*/
    private fun deleteComment(commentId: String){
        DialogFunc.deletePostInfoDialog(context, isPost = false){
            postViewModel.deleteComment(commentId, pref.getUserId())
        }
    }
}