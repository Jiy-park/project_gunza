package com.example.project_gunza.gunza_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.project_gunza.databinding.ActivityCourseBinding

/** 대학변 원격 강의 개설 과목 확인*/
class Course : AppCompatActivity() {
    private val binding by lazy { ActivityCourseBinding.inflate(layoutInflater) }
    private val context by lazy { binding.root.context }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun setViewEvent() = with(binding){
        layerTopPanel.btnBack.setOnClickListener { finish() }
        btnSearch.setOnClickListener { searchCourse() }
    }

    private fun searchCourse(){
        Toast.makeText(context, "무언가를 검색한다...", Toast.LENGTH_SHORT).show()
    }
}