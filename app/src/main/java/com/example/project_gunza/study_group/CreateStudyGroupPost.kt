package com.example.project_gunza.study_group

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project_gunza.*
import com.example.project_gunza.common.EXCEPTION
import com.example.project_gunza.common.INTENT
import com.example.project_gunza.common.Preference
import com.example.project_gunza.data_class.PostStruct
import com.example.project_gunza.databinding.ActivityCreateStudyGroupPostBinding
import java.text.SimpleDateFormat

class CreateStudyGroupPost : AppCompatActivity() {
    private val binding by lazy { ActivityCreateStudyGroupPostBinding.inflate(layoutInflater) }
    private lateinit var context: Context
    private lateinit var pref: Preference

    private var authorId = ""
    private var groupId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        setViewEvent()
    }

    private fun init(){
        context = binding.root.context
        pref = Preference(context)
        authorId = pref.getUserId()

        groupId = intent.getStringExtra(INTENT.GROUP.ID)?: EXCEPTION.NOT_FOUND_GROUP_ID
    }

    private fun setViewEvent(){
        with(binding){
            layerTopPanel.btnBack.setOnClickListener { finish() }
            btnDone.setOnClickListener { createPost() }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createPost(){
        val content = binding.editContent.text.toString()
        val postTitle = binding.editTitle.text.toString()
        if(content.isEmpty()) { Toast.makeText(context, "내용을 입력 해야 합니다.", Toast.LENGTH_SHORT).show() }
        else if(postTitle.isEmpty()) { Toast.makeText(context, "제목을 입력 해야 합니다.", Toast.LENGTH_SHORT).show() }
        else{
            val currentTime = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(System.currentTimeMillis())
            val postId = postTitle + currentTime
            val post = PostStruct(
                postId = postId,
                authorId = authorId,
                groupId = groupId,
                postTitle = postTitle,
                createdAt = currentTime,
                content = content,
                like = 0,
                comment = listOf(),
            )

            val intent = Intent(this@CreateStudyGroupPost, StudyGroup::class.java)
            intent.putExtra(INTENT.POST.CREATE_POST, post)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}