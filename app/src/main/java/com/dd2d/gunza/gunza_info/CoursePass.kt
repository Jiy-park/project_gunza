package com.dd2d.gunza.gunza_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dd2d.gunza.databinding.ActivityCoursePassBinding

class CoursePass : AppCompatActivity() {
    private val binding by lazy { ActivityCoursePassBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.layerTopPanel.btnBack.setOnClickListener { finish()}
    }
}