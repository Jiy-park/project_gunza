package com.example.project_gunza.data_class

import com.example.project_gunza.common.VALUE

data class UserStruct(
    val userId: String = "",
    val userName: String = "",
    val userUnit: String = "",
    val userProfile: String = "",
    val userMsg: String = "",
    val userExp: Int = VALUE.LEVEL_CONVERT_VAL,
    val recentVisit: MutableMap<String, Long> = mutableMapOf(),
    val createPost: MutableList<String> = mutableListOf(),
    val createGroup: MutableList<String> = mutableListOf(),
    val createComment: MutableList<String> = mutableListOf(),
    val userLikePost: MutableList<String> = mutableListOf(),
)