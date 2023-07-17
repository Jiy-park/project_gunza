package com.dd2d.gunza.study_room

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dd2d.gunza.common.FIELD
import com.dd2d.gunza.common.VALUE
import com.dd2d.gunza.data_class.SimpleStudyGroupStruct
import com.dd2d.gunza.data_class.StudyGroupStruct
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/** 스터디 룸 모델*/
class StudyRoomRepository{
    private val db = Firebase.firestore.collection(FIELD.GROUP.ROOT)

    val roomInfo = MutableLiveData<List<SimpleStudyGroupStruct>>()

    /** 스터디 그룹 생성*/
    fun createStudyGroup(group: StudyGroupStruct, callback: ()->Unit){
        db.document(group.groupId)
            .set(group)
            .addOnSuccessListener {
                callback()
            }
    }

    /** 파라미터에 해당하는 그룹 리스트 반환
     * @param searchTitle 요구하는 그룹 이름*/
    fun searchStudyGroup(searchTitle: String) =
        roomInfo.value?.filter {
            it.groupTitle.contains(searchTitle)
        }?: emptyList()

    /** 그룹을 정렬해서 리스트 형태로 반환
     * @param sortBy 정렬 기준
     * @return 정렬된 리스트 */
    fun sortStudyGroup(sortBy: Int, userVisitInfo: List<Pair<String, Long>>): List<SimpleStudyGroupStruct> = with(
        VALUE
    ){
        var sortedList = mutableListOf<SimpleStudyGroupStruct>()
        when(sortBy){
            SORT_BY_RECENT_VISIT -> { // 유저가 최근 방문한 그룹 기준 정렬
                userVisitInfo.forEach { visitGroup ->
                    val group = roomInfo.value?.find { group-> group.groupId == visitGroup.first }
                    group?.let { sortedList.add(it) }
                }
            }

            SORT_BY_NUM_OF_GROUP_MEMBER -> { // 그룹 내 인원이 많은 기준 정렬
                sortedList = roomInfo.value?.sortedByDescending {
                    it.groupMemberId.size
                }?.toMutableList() ?: mutableListOf()
            }
        }

        Log.d("LOG_CHECK", "StudyRoomViewModel :: sortStudyGroup() -> sortedList : $sortedList")
        return sortedList
    }

    /** studyGroup db로 부터 데이터 가져옴: 최초 1회만 실행 */
    private fun fetchData(){
        db.orderBy(FIELD.GROUP.POST_ID_LIST, Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            error?.let {
                Log.e("LOG_CHECK", "StudyRoomViewModel :: error in link to studyRoom db -> ERROR ${it.message}")
            }?: run{
                roomInfo.value = value?.toList()?.map { it.toObject(SimpleStudyGroupStruct::class.java) }
            }
        }
    }

    companion object{
        private var instance: StudyRoomRepository? = null

        fun getInstance(): StudyRoomRepository{
            return instance?: synchronized(this){
                instance?: StudyRoomRepository().also {
                    Log.d("LOG_CHECK", "StudyRoomRepository :: getInstance() -> init ")
                    it.fetchData()
                    instance = it
                }
            }
        }
    }
}