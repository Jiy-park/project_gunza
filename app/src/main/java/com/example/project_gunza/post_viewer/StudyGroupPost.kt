package com.example.project_gunza.post_viewer

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project_gunza.*
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.common.INTENT
import com.example.project_gunza.common.Preference
import com.example.project_gunza.common.VALUE
import com.example.project_gunza.data_class.Comment
import com.example.project_gunza.data_class.PostAuthorStruct
import com.example.project_gunza.data_class.PostStruct
import com.example.project_gunza.databinding.ActivityStudyGroupPostViewerBinding
import com.example.project_gunza.main_page.UserViewModel
import java.text.SimpleDateFormat

class StudyGroupPost : AppCompatActivity() {
    private lateinit var binding: ActivityStudyGroupPostViewerBinding
    private lateinit var context: Context
    private lateinit var pref: Preference

    private lateinit var postCommentViewModel: PostCommentViewModel
    private lateinit var userViewModel: UserViewModel

    private var post: PostStruct? = null
    private var author: PostAuthorStruct? = null

    private lateinit var commentAdapter: PostCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        setViewEvent()
        getDataFromIntent()
        setViewModel()

        binding.recyclerviewComment.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = commentAdapter
        }
    }

    private fun init(){
        binding = DataBindingUtil.setContentView(this@StudyGroupPost, R.layout.activity_study_group_post_viewer)
        context = binding.root.context
        pref = Preference(context)
        commentAdapter = PostCommentAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel(){
        userViewModel = UserViewModel(pref.getUserId())

        postCommentViewModel = PostCommentViewModel(post!!.postId)
        postCommentViewModel.postInfo.observe(this@StudyGroupPost){ commentList ->
            binding.commentCount = commentList.size
            commentAdapter.commentList = commentList
            commentAdapter.notifyDataSetChanged()
        }

    }

    /** * 인텐트로 넘어온 데이터를 받아옴*/
    private fun getDataFromIntent(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            post = intent.getParcelableExtra(INTENT.POST.POST, PostStruct::class.java)
            author = intent.getParcelableExtra(INTENT.POST.AUTHOR, PostAuthorStruct::class.java)
        }
        else {
            post = intent.getParcelableExtra(INTENT.POST.POST)
            author = intent.getParcelableExtra(INTENT.POST.AUTHOR)
        }


        if(post == null || author == null) { postNotFound() }
        else{
            binding.post = post
            binding.author = author
        }
    }

    private fun setViewEvent(){
        binding.btnDone.setOnClickListener { createComment() }
        binding.btnLike.setOnClickListener {
            if(checkLike()){ alreadyLikePost() }
            else { likePost() }
        }
    }

    /** 유저가 좋아요 버튼을 눌렀었는지 체크
     * @return true-> 좋아요 눌렀음 */
    private fun checkLike(): Boolean{
        val userLikeList = userViewModel.userInfo.value?.userLikePost?: emptyList()

        return if(userLikeList.contains(post?.postId)) {
            Glide.with(context).load(R.drawable.ic_heart).into(binding.ivHeart)
            true
        }else{
            Glide.with(context).load(R.drawable.ic_silver_heart).into(binding.ivHeart)
            false
        }
    }

    /** 게시글 좋아요 버튼*/
    private fun likePost(){
        postCommentViewModel.likePost(post?.postId!!){
            userViewModel.updateUserInfo(FIELD.USER.LIKE_POST, post?.postId!!, FIELD.TYPE.LIST)
            binding.btnLike.setOnClickListener { alreadyLikePost() }
        }
    }

    /** 이미 좋아요 버튼을 누르고 있음*/
    private fun alreadyLikePost(){
        Toast.makeText(context, "뭐하지..???", Toast.LENGTH_SHORT).show()
    }

    /** 게시글에 댓글 달기*/
    @SuppressLint("SimpleDateFormat")
    private fun createComment(){
        with(userViewModel.userInfo.value!!){
            val content = binding.editContent.text.toString()
            if(content.isEmpty()) { Toast.makeText(context, "내용을 입력해 주세요.", Toast.LENGTH_SHORT).show() }
            else{
                val currentTime = SimpleDateFormat("yyyy:MM:dd  HH:mm:ss").format(System.currentTimeMillis())
                val postId = post!!.postId
                val userId = userId
                val userLevel = userExp/ VALUE.LEVEL_CONVERT_VAL
                val userName = userName
                val userUnit = userUnit
                val comment = Comment(
                    commentId = userId + postId + currentTime,
                    postId = postId,
                    authorId = userId,
                    authorName = userName,
                    authorUnit = userUnit,
                    authorLevel = userLevel,
                    createdAt = currentTime,
                    commentBody = content,
                )

                postCommentViewModel.createComment(comment){
                    binding.editContent.text.clear()
                    postCommentViewModel.updateByCreateComment(userId, postId, comment.commentId)
                }
            }
        }
    }


    /** * 포스트를 찾지 못한 경우 호출*/
    private fun postNotFound() {
//        TODO("게시글 못찾았을 때 표기할 거 ")
    }
}