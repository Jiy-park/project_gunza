package com.example.project_gunza.study_room

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.common.Preference
import com.example.project_gunza.common.VALUE
import com.example.project_gunza.common.ViewModelFactory
import com.example.project_gunza.data_class.*
import com.example.project_gunza.databinding.ActivityStudyRoomBinding
import com.example.project_gunza.dialog.DialogFunc
import com.example.project_gunza.main_page.UserViewModel
import java.text.SimpleDateFormat

class StudyRoom : AppCompatActivity() {
    private val binding by lazy { ActivityStudyRoomBinding.inflate(layoutInflater) }
    private lateinit var context: Context

    private lateinit var studyGroupAdapter: StudyRoomAdapter

    private lateinit var roomViewModel: StudyRoomViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = binding.root.context
        studyGroupAdapter = StudyRoomAdapter()

        setViewModel()
        setViewEvent()

        binding.recyclerviewStudyRoom.apply {
            adapter = studyGroupAdapter
            layoutManager = GridLayoutManager(context, VALUE.NUM_OF_VIEW_GRID)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel(){
        userViewModel = ViewModelProvider(this@StudyRoom, ViewModelFactory(Preference(context).getUserId()))[UserViewModel::class.java]

        roomViewModel = ViewModelProvider(this@StudyRoom)[StudyRoomViewModel::class.java].apply {
            roomInfo.observe(this@StudyRoom){ roomList ->
                studyGroupAdapter.studyGroupList = roomList
                studyGroupAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setViewEvent(){
        binding.layerTopPanel.btnBack.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener { searchGroup() }
        binding.btnCreate.setOnClickListener { createStudyGroup() }
        binding.btnSort.setOnClickListener { sortStudyGroup() }
        binding.btnBackToMain.setOnClickListener { backToMain(it) }
    }

    /** 검색 그룹 보기 -> 모든 그룹 보기 전환*/
    @SuppressLint("NotifyDataSetChanged")
    private fun backToMain(view: View){
        studyGroupAdapter.studyGroupList = roomViewModel.roomInfo.value?: emptyList()
        studyGroupAdapter.notifyDataSetChanged()
        view.visibility = View.GONE
        roomViewModel.defaultValue()
        binding.tvActivityTitle.text = "스터디룸"
        binding.tvSortedBy.text = "최신순"
    }

    /** * 스터디 그룹을 만듦
     *- 그룹 아이디 : 그룹 타이틀 + 생성 시간
     *- ex) "내그룹_23:07:01-16:12:14"
     *- 다이얼로그 에서 그룹 타이틀 받아옴 */
    @SuppressLint("SimpleDateFormat")
    private fun createStudyGroup(){
        if(checkCreatedGroupByUser()){
            DialogFunc.createStudyGroup(context){ groupTitle ->
                with(userViewModel.userInfo.value!!){
                    val currentTime = SimpleDateFormat("yy:MM:dd-HH:mm:ss").format(System.currentTimeMillis())
                    val creatorId = userId
                    val creatorUnit = userUnit
                    val groupId = "${groupTitle}_${creatorId}_$currentTime"
                    val studyGroupStruct = StudyGroupStruct(
                        groupId = groupId,
                        creatorId = userId,
                        createdAt = System.currentTimeMillis(),
                        groupTitle = groupTitle,
                        memberUnit = mapOf(creatorUnit to 1),
                        groupMemberId = listOf(creatorId),
                        groupPostId = emptyList(),
                    )
                    roomViewModel.createStudyGroup(studyGroupStruct){
                        userViewModel.updateUserInfo(FIELD.USER.CREATE, groupId, FIELD.TYPE.LIST)
                        userViewModel.updateUserInfo(FIELD.USER.EXP, VALUE.EXP_OF_ONE_GROUP, FIELD.TYPE.NORMAL)
                    }
                }
            }
        }
        else{
            Toast.makeText(context, "그룹은 최대 ${VALUE.CREATE_GROUP_LIMIT} 개 생성 가능 합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCreatedGroupByUser(): Boolean{
        val createdGroupCount = userViewModel.userInfo.value?.createGroup?.size ?: 0
        return createdGroupCount < VALUE.CREATE_GROUP_LIMIT
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun searchGroup(){
        DialogFunc.searchDialog(context, VALUE.SEARCH_STUDY_GROUP){ searchTitle ->
            val list = roomViewModel.searchStudyGroup(searchTitle)

            if(list.isEmpty()) { Toast.makeText(context, "해당되는 그룹이 없습니다.", Toast.LENGTH_SHORT).show() }
            else {
                binding.tvActivityTitle.text = "\"$searchTitle\" 검색 결과"
                binding.btnBackToMain.visibility = View.VISIBLE
                studyGroupAdapter.studyGroupList = list
                studyGroupAdapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortStudyGroup(){
        DialogFunc.sortStudyGroup(context){ sortBy, sortText ->
            val userVisitInfo =
                if(sortBy == VALUE.SORT_BY_RECENT_VISIT){
                    userViewModel
                        .userInfo.value?.recentVisit?.let { map->
                            map.toList().sortedByDescending { visitGroup ->
                                visitGroup.second
                            }
                        }?: emptyList()
                }
                else { emptyList() }
            
            val sortedList = roomViewModel.sortStudyGroup(sortBy, userVisitInfo)

            if(sortedList.isEmpty()) { Log.e("LOG_CHECK", "StudyRoom :: sortStudyGroup()  -> error in sort") }
            else {
                binding.tvSortedBy.text = sortText
                binding.btnBackToMain.visibility = View.VISIBLE
                studyGroupAdapter.studyGroupList = sortedList
                studyGroupAdapter.notifyDataSetChanged()
            }
        }
    }
}