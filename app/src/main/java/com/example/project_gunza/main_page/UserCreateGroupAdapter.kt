package com.example.project_gunza.main_page

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_gunza.data_class.SimpleStudyGroupStruct
import com.example.project_gunza.databinding.CommonGroupViewerBinding

class UserCreateGroupAdapter: RecyclerView.Adapter<UserCreateGroupAdapter.Holder>() {
    private lateinit var binding: CommonGroupViewerBinding

    var userCreateGroupList = listOf<SimpleStudyGroupStruct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = CommonGroupViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
//        if(userCreateGroupList.isNotEmpty()) { holder.bind(userCreateGroupList[position]) }
    }

    override fun getItemCount() = userCreateGroupList.size

    inner class Holder(val binding: CommonGroupViewerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(group: CommonGroupViewerBinding){
            binding
        }
    }
}