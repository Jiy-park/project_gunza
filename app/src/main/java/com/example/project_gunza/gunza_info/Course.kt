package com.example.project_gunza.gunza_info

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.databinding.ActivityCourseBinding
import com.example.project_gunza.dialog.DialogFunc
import org.json.JSONArray
import org.json.JSONObject

/** 대학변 원격 강의 개설 과목 확인*/
class Course : AppCompatActivity() {
    private val binding by lazy { ActivityCourseBinding.inflate(layoutInflater) }
    private val context by lazy { binding.root.context }

    private lateinit var courseAdapter: CourseAdapter
    private var jsonList = JSONArray()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        courseAdapter = CourseAdapter()

        setViewEvent()
        getJson()

        binding.recyclerviewCourse.apply {
            layoutManager = LinearLayoutManager(this@Course, LinearLayoutManager.VERTICAL, false)
            adapter = courseAdapter
        }
    }

    private fun setViewEvent() = with(binding){
        layerTopPanel.btnBack.setOnClickListener { finish() }
        btnSearch.setOnClickListener { searchCourse() }
        btnBackToMain.setOnClickListener { backToMain() }
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun searchCourse(){
        DialogFunc.searchDialog(context, "검색할 대학교 입력"){ univName ->
            val list = searchUniv(univName)

            if(list.isEmpty()) { Toast.makeText(context, "해당되는 대학이 없습니다.", Toast.LENGTH_SHORT).show() }
            else {
                binding.tvSearchResult.text = "\"$univName\" 검색 결과"
                binding.tvSearchResult.visibility = View.VISIBLE
                binding.btnBackToMain.visibility = View.VISIBLE
                courseAdapter.courseList = list
                courseAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun backToMain(){
        binding.tvSearchResult.visibility = View.GONE
        binding.btnBackToMain.visibility = View.GONE
        courseAdapter.courseList = emptyList()
        courseAdapter.notifyDataSetChanged()
    }

    private fun getJson(){
        val json = assets.open("daehak.json").reader().readText()
        jsonList = JSONArray(json)
    }

    private fun searchUniv(name: String): List<JSONObject>{
        val result = mutableListOf<JSONObject>()

        for(i in 0 until jsonList.length()){
            val jsonObject = jsonList.getJSONObject(i)
            val univName = jsonObject.optString(FIELD.JSON.UNIVERSITY_NAME)

            if(univName.contains(name)){
                result.add(jsonObject)
            }
        }
        return result
    }
}