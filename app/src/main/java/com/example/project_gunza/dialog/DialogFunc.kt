package com.example.project_gunza.dialog

import android.content.Context
import android.view.LayoutInflater
import android.widget.*
import com.example.project_gunza.R
import com.example.project_gunza.common.VALUE
import com.example.project_gunza.databinding.DialogSearchStudyGroupBinding
import com.example.project_gunza.databinding.DialogSortStudyGroupBinding

class DialogFunc {
    companion object{
        fun createStudyGroup(context: Context, createStudyGroup: (groupTitle: String)->Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_create_study_group, null)
            val edit = view.findViewById<EditText>(R.id.editStudyGroupName)

            with(dialog){
                title = "생성"
                leftBtnName = "확인"
                rightBtnName = "취소"

                setLeftBtnClickListener {
                    if(edit.text.isEmpty()) { Toast.makeText(context, "그룹의 이름을 정해야 합니다.", Toast.LENGTH_SHORT).show() }
                    else{
                        Toast.makeText(context, "스터디 그룹을 만들었습니다.", Toast.LENGTH_SHORT).show()
                        createStudyGroup(edit.text.toString())
                        dismiss()
                    }
                }
                this.view = view
                initDialog()
            }
        }

        fun sortStudyGroup(context: Context, callback:(sortBy: Int, sortText: String)->Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_sort_study_group, null)
            val binding = DialogSortStudyGroupBinding.bind(view) // 데이터 바인딩 적용

            with(dialog){
                title = "정렬"
                leftBtnName = "확인"
                rightBtnName = "취소"

                setLeftBtnClickListener {
                    with(VALUE){
                        var sortText = ""
                        val sortBy = when (binding.rgSort.checkedRadioButtonId){
                            binding.rbSortByRecentVisit.id -> {
                                sortText = "최근 방문순"
                                SORT_BY_RECENT_VISIT
                            }
                            binding.rbSortByGroupMember.id -> {
                                sortText = "그룹 인원순"
                                SORT_BY_NUM_OF_GROUP_MEMBER
                            }
                            else -> {
                                NOT_SELECTED_SORT
                            }
                        }
                        callback(sortBy, sortText)
                        dismiss()
                    }
                }
                this.view = view
                initDialog()
            }
        }

        fun searchDialog(context: Context, hintTxt: String, callback: (searchTitle: String)->Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_search_study_group, null)
            val binding = DialogSearchStudyGroupBinding.bind(view)
            binding.editSearchTitle.hint = hintTxt

            with(dialog){
                title = "검색"
                leftBtnName = "확인"
                rightBtnName = "취소"

                setLeftBtnClickListener {
                    val searchTitle = binding.editSearchTitle.text.toString()
                    if(searchTitle.isEmpty()) { Toast.makeText(context, "검색어를 입력 해야 합니다.", Toast.LENGTH_SHORT).show() }
                    else{
                        callback(searchTitle)
                        dismiss()
                    }
                }
                this.view = view
                initDialog()
            }
        }

    }
}