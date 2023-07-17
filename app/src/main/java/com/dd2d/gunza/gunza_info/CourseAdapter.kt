package com.dd2d.gunza.gunza_info

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dd2d.gunza.common.FIELD
import com.dd2d.gunza.databinding.ItemUnivCourseBinding
import org.json.JSONObject

class CourseAdapter: RecyclerView.Adapter<CourseAdapter.Holder>() {
    private lateinit var binding: ItemUnivCourseBinding
    var courseList = listOf<JSONObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemUnivCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(courseList.isNotEmpty()){
            holder.bind(courseList[position])
        }
    }

    override fun getItemCount() = courseList.size

    inner class Holder(val binding: ItemUnivCourseBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(course: JSONObject){
            binding.univName.text = course.getString(FIELD.JSON.UNIVERSITY_NAME)
            binding.subName.text = course.getString(FIELD.JSON.SUBJECT_NAME)
            binding.subCredit.text = course.getString(FIELD.JSON.CREDIT)
        }
    }
}