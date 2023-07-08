package com.example.project_gunza.study_room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_gunza.common.VALUE
import com.example.project_gunza.data_class.SimpleStudyGroupStruct
import com.example.project_gunza.data_class.StudyGroupStruct

/** 스터디 룸 뷰모델*/
class StudyRoomViewModel: ViewModel() {
    private val studyRoomRepository = StudyRoomRepository.getInstance()

    private var _roomInfo = MutableLiveData<List<SimpleStudyGroupStruct>>()
    val roomInfo: LiveData<List<SimpleStudyGroupStruct>>
        get() = _roomInfo

    /** 데이터를 불러 올 때 최신 게시글이 등록된 그룹 순으로 정렬해서 불러옴*/
    init {
        Log.d("LOG_CHECK", "StudyRoomViewModel :: () -> init viewModel")
        _roomInfo = studyRoomRepository.roomInfo
    }

    /** @see StudyRoomRepository.createStudyGroup*/
    fun createStudyGroup(group: StudyGroupStruct, callback: ()->Unit){
        studyRoomRepository.createStudyGroup(group){
            callback()
        }
    }

    /** 그룹 이름에 파라미터가 포함된 그룹을 찾음
     * @param searchTitle 포함될 단어
     * @see StudyRoomRepository.searchStudyGroup*/
    fun searchStudyGroup(searchTitle: String) = studyRoomRepository.searchStudyGroup(searchTitle)


    /** 그룹을 [sortBy] 기준 정렬
     * @param[sortBy]
     * @sample [VALUE.SORT_BY_NUM_OF_GROUP_MEMBER]*/
    fun sortStudyGroup(sortBy: Int, userVisitInfo: List<Pair<String, Long>>) = studyRoomRepository.sortStudyGroup(sortBy, userVisitInfo)


    /** 정렬 또는 검색 된 데이터를 기존 데이터로 변경*/
    fun defaultValue(){
        _roomInfo = studyRoomRepository.roomInfo
    }
}
