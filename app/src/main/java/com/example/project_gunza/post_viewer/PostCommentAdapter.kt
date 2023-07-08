package com.example.project_gunza.post_viewer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.project_gunza.common.Preference
import com.example.project_gunza.data_class.CommentStruct
import com.example.project_gunza.data_class.SimpleUserStruct
import com.example.project_gunza.databinding.ItemPostCommentViewerBinding
import com.example.project_gunza.dialog.DialogFunc
import com.example.project_gunza.post_viewer.PostCommentAdapter.Holder

class PostCommentAdapter(
        val update: (commentId: String, newContent: String)-> Unit,
        val delete: (commentId: String)->Unit
    ) :RecyclerView.Adapter<Holder>() {
    private lateinit var binding: ItemPostCommentViewerBinding
    var commentList = listOf<CommentStruct>()
    var authorList = listOf<SimpleUserStruct>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = ItemPostCommentViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(commentList.isNotEmpty() && authorList.isNotEmpty()){
            holder.bind(commentList[position], authorList[position])
        }
    }

    override fun getItemCount() = commentList.size

    inner class Holder(private val binding: ItemPostCommentViewerBinding): RecyclerView.ViewHolder(binding.root){
        private val context = binding.root.context
        private val pref = Preference(context)
        private var commentId = ""

        init{
            binding.commentModify.setOnClickListener { modifyComment() }
            binding.completeModify.setOnClickListener { completeModify() }
            binding.cancelModify.setOnClickListener { cancelModify() }
            binding.completeDelete.setOnClickListener { deleteComment() }
        }
        fun bind(comment: CommentStruct, author: SimpleUserStruct){
            commentId = comment.commentId

            binding.comment = comment
            binding.author = author

            binding.isAuthor = author.userId == pref.getUserId()
            binding.modifyMode = false
        }

        fun modifyComment() {
            binding.modifyMode= true
            binding.editCommentContent.requestFocus()
        }
        fun cancelModify() { binding.modifyMode = false }

        /** 댓글 수정
         * [update], [delete] : [StudyGroupPost] 에서 처리하기 위함
         * @see PostViewModel.updateComment*/
        fun completeModify() {
            val newContent = binding.editCommentContent.text.toString()

            if(newContent.isEmpty()) {
                Toast.makeText(context, "내용을 입력해야 합니다.", Toast.LENGTH_SHORT).show()
            }else{
                binding.modifyMode = false
                update(commentId, newContent)
            }
        }

        /** 댓글 삭제
         * [update], [delete] : [StudyGroupPost] 에서 처리하기 위함
         * @see PostViewModel.deleteComment*/
        fun deleteComment() {
            delete(commentId)
        }
    }
}