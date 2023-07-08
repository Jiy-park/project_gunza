package com.example.project_gunza.data_class

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class StudyGroupStruct(
    val groupId: String = "",
    val creatorId: String = "",
    val createdAt: Long = 0L,
    val groupTitle: String = "",
    val memberUnit: Map<String, Int> = mapOf(),
    val groupMemberId: List<String> = emptyList(),
    val groupPostId: List<String> = emptyList(),
)

///** * 스터디 룸 -> 그룹 이동시 보여질 스터디 그룹 정보*/
//@Parcelize
//data class DetailStudyGroupStruct(
//): Parcelable

/** * 스터디 룸 내에서 보여질 그룹의 정보*/
@Parcelize
data class SimpleStudyGroupStruct(
    val groupId: String = "",
    val groupTitle: String = "",
    val memberUnit: Map<String, Int> = mapOf(),
    val groupMemberId: List<String> = emptyList(),
    val groupPostId: List<String> = emptyList(),
): Parcelable