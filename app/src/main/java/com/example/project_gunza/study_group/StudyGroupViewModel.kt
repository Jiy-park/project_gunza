package com.example.project_gunza.study_group

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.*
import com.example.project_gunza.data_class.SimpleUserStruct
import com.example.project_gunza.data_class.PostStruct
import kotlinx.coroutines.*

/** 그룹 뷰모델
 * @param mostRecentPostTimeStamp 가장 최근에 올라온 게시글의 시간을 저장, 새로고침 시 해당 시간 이후에 만들어진 게시글만 다운*/
@SuppressLint("SimpleDateFormat")
class StudyGroupViewModel(private val groupId: String): ViewModel() {
    private var studyGroupRepository = StudyGroupRepository()

    private var _groupInfo = MutableLiveData<List<PostStruct>>()
    val groupInfo: LiveData<List<PostStruct>>
        get() = _groupInfo

    var authorInfo = listOf<SimpleUserStruct>()

    init {
        Log.d("LOG_CHECK", "StudyGroupViewModel :: () -> init viewModel")
        refresh()
    }

    fun defaultValue(){
        authorInfo = studyGroupRepository.authorInfo
        _groupInfo.value = studyGroupRepository.groupPostInfo
    }

    /** 그룹 내 게시글 검색
     * @see StudyGroupRepository.searchPostByTitle*/
    fun searchPostByTitle(searchTitle: String, callback: () -> Unit){
        viewModelScope.launch {
            studyGroupRepository.searchPostByTitle(searchTitle)?.let { res ->
                authorInfo = res.second
                _groupInfo.value = res.first
                callback()
            }
        }
    }

    fun createPost(post: PostStruct, callback: ()->Unit){
        studyGroupRepository.createGroupPost(post){
            callback()
        }
    }

    /** @see StudyGroupRepository.viewMyPost */
    fun viewMyPost(authorId: String, callback: () -> Unit){
        viewModelScope.launch {
            studyGroupRepository.viewMyPost(authorId)?.let { res ->
                authorInfo = res.second
                _groupInfo.value = res.first
                callback()
            }
        }
    }

    fun refresh(){
        viewModelScope.launch(Dispatchers.IO) {
            studyGroupRepository.fetchData(groupId)
            withContext(Dispatchers.Main){
                authorInfo = studyGroupRepository.authorInfo
                _groupInfo.value = studyGroupRepository.groupPostInfo
            }
        }
    }

    /** 그룹 데이터 업데이트
     * @see StudyGroupRepository.updateGroupInfo*/
    fun updateGroupInfo(groupId: String, field: String, value: Any, fieldType: String, key: String? = null){
        studyGroupRepository.updateGroupInfo(groupId, field, value, fieldType, key)
    }
}