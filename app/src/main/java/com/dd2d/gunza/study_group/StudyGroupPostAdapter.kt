package com.dd2d.gunza.study_group

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dd2d.gunza.common.INTENT
import com.dd2d.gunza.data_class.SimpleUserStruct
import com.dd2d.gunza.data_class.PostStruct
import com.dd2d.gunza.databinding.ItemStudyGroupPostSimpleViewerBinding
import com.dd2d.gunza.post_viewer.StudyGroupPost

class StudyGroupPostAdapter: RecyclerView.Adapter<StudyGroupPostAdapter.Holder>() {
    private lateinit var binding: ItemStudyGroupPostSimpleViewerBinding
    var postList = listOf<PostStruct>()
    var authorList = listOf<SimpleUserStruct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemStudyGroupPostSimpleViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(postList.isNotEmpty()) { holder.bind(postList[position], authorList[position]) }
    }

    override fun getItemCount() = postList.size

    inner class Holder(private val binding: ItemStudyGroupPostSimpleViewerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(post: PostStruct, author: SimpleUserStruct){
            binding.post = post
            binding.author = author
            setViewEvent(post)
        }
        private fun setViewEvent(post: PostStruct){
            itemView.setOnClickListener {
                val intent = Intent(binding.root.context, StudyGroupPost::class.java).apply {
                    putExtra(INTENT.POST.ID, post.postId)
                }
                binding.root.context.startActivity(intent)
            }
        }
    }
}