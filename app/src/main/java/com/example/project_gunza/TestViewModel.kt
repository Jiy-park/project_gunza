package com.example.project_gunza

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.data_class.UserStruct
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TestUserRepo{
    companion object{
        private var instance: TestUserRepo? = null

        fun getInstance(userId: String): TestUserRepo{
            return instance?: synchronized(this){
                instance?: TestUserRepo().also {
                    it.fetchData(userId)
                    instance = it
                }
            }
        }
    }
    private val db = Firebase.firestore.collection(FIELD.USER.ROOT)

    var userInfo = MutableLiveData<UserStruct>()

    private fun fetchData(userId: String){
        Log.d("LOG_CHECK", "TestUserRepo :: fetchData() -> fetch")
        db.document(userId)
            .addSnapshotListener { value, error ->
                error?.let {
                    Log.e("LOG_CHECK", "TestUserRepo :: getUserInfo() error in link to user db -> ${it.message} ")
                }?: run{
                    userInfo.value = value?.toObject(UserStruct::class.java)
                }
        }
    }
}

class TestViewModel(userId: String): ViewModel() {
    private var userRepo = TestUserRepo.getInstance(userId)
    private var _userInfo = MutableLiveData<UserStruct>()
    val userInfo: LiveData<UserStruct>
        get() = _userInfo

    init {
        _userInfo = userRepo.userInfo
    }

    fun updateUserName(newName: String){

    }
}