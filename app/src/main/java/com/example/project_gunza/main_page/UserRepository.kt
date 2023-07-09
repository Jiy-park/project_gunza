package com.example.project_gunza.main_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.data_class.UserStruct
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/** 유저 모델*/
class UserRepository{
    private val db = Firebase.firestore.collection(FIELD.USER.ROOT)
    private var userId = ""
    var userInfo = MutableLiveData<UserStruct>()

    /** * 유저에 데이터 추가
     * @param fieldType == [FIELD.TYPE.MAP] 일 때는 [keyValue] 가 null 이면 안됨*/
    fun addUserInfo(field: String, value: Any, fieldType: String, keyValue: String? = null){
        Log.d("LOG_CHECK", "UserRepository :: updateUserInfo() -> update")
        db.document(userId)
            .run{
                when(fieldType){
                    FIELD.TYPE.NORMAL -> {
                        when(value){
                            is Long -> { this.update(field, FieldValue.increment(value)) }
                            is String -> { this.update(field, value) }
                            else -> {}
                        }
                    }
                    FIELD.TYPE.LIST -> { this.update(field, FieldValue.arrayUnion(value)) }
                    FIELD.TYPE.MAP -> {
                        keyValue?.let { key->
                            this.update("$field.$key", value)
                        }?: run{ Log.e("LOG_CHECK", "UserRepository :: updateUserInfo() -> key is null!!!") }
                        }
                    else -> {}
                }
            }
    }

    /** * 유저 데이터 삭제*/
    fun removeUserInfo(field: String, value: Any, fieldType: String){  //, keyValue: String? = null){
        Log.d("LOG_CHECK", "UserRepository :: updateUserInfo() -> update")
        db.document(userId)
            .run{
                when(fieldType){
                    FIELD.TYPE.NORMAL -> { this.update(field, FieldValue.increment(-(value as Long))) }
                    FIELD.TYPE.LIST -> { this.update(field, FieldValue.arrayRemove(value)) }
                    FIELD.TYPE.MAP -> {
//                        keyValue?.let { key->
//                            this.update("$field.$key", value)
//                        }?: run{ Log.e("LOG_CHECK", "UserRepository :: updateUserInfo() -> key is null!!!") }
                    }
                    else -> {}
                }
            }
    }

    /** user db로 부터 데이터 가져옴: 최초 1회만 실행 */
    private fun fetchData(userId: String){
        Log.d("LOG_CHECK", "UserRepository :: fetchData() -> fetch user($userId) data")
        db.document(userId)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e("LOG_CHECK", "UserRepository :: fetchData() error in link to user db-> ${it.message}")
                }?: run{
                    userInfo.value = value?.toObject(UserStruct::class.java)
                }
            }
    }

    /** 로그 아웃 시 연결 됐던 유저 데이터를 지움*/
    fun deleteInstance(){
        instance = null
        Log.d("LOG_CHECK", "UserRepository :: deleteInstance() -> delete user repository instance")
    }

    companion object{
        private var instance: UserRepository? = null

        fun getInstance(userId: String): UserRepository {
            return instance ?: synchronized(this){
                instance ?: UserRepository().also {
                    Log.d("LOG_CHECK", "UserRepository :: getInstance() -> init")
                    it.userId = userId
                    it.fetchData(userId)
                    instance = it
                }
            }
        }
    }
}