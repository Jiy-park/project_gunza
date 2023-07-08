package com.example.project_gunza.post_viewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_gunza.data_class.Comment
import com.example.project_gunza.databinding.ItemPostCommentViewerBinding
import com.example.project_gunza.post_viewer.PostCommentAdapter.Holder

class PostCommentAdapter:RecyclerView.Adapter<Holder>() {
    private lateinit var binding: ItemPostCommentViewerBinding
    var commentList = listOf<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemPostCommentViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(commentList.isNotEmpty()) { holder.bind(commentList[position]) }
    }

    override fun getItemCount() = commentList.size

    inner class Holder(private val binding: ItemPostCommentViewerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(comment: Comment){
            binding.comment = comment
        }
    }
}