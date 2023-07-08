package com.example.project_gunza.study_group

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_gunza.common.INTENT
import com.example.project_gunza.data_class.PostAuthorStruct
import com.example.project_gunza.data_class.PostStruct
import com.example.project_gunza.databinding.ItemStudyGroupPostSimpleViewerBinding
import com.example.project_gunza.post_viewer.StudyGroupPost

class StudyGroupPostAdapter: RecyclerView.Adapter<StudyGroupPostAdapter.Holder>() {
    private lateinit var binding: ItemStudyGroupPostSimpleViewerBinding
    var postList = listOf<PostStruct>()
    var authorList = listOf<PostAuthorStruct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemStudyGroupPostSimpleViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(postList.isNotEmpty()) { holder.bind(postList[position], authorList[position]) }
    }

    override fun getItemCount() = postList.size

    inner class Holder(private val binding: ItemStudyGroupPostSimpleViewerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: PostStruct, author: PostAuthorStruct){
            binding.post = post
            binding.author = author
            setViewEvent(post, author)
        }
        private fun setViewEvent(post: PostStruct, author: PostAuthorStruct){
            itemView.setOnClickListener {
                val intent = Intent(binding.root.context, StudyGroupPost::class.java)
                intent.putExtra(INTENT.POST.POST, post)
                intent.putExtra(INTENT.POST.AUTHOR, author)
                binding.root.context.startActivity(intent)
            }
        }
    }
}