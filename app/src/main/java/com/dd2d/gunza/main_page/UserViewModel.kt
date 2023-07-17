package com.dd2d.gunza.main_page

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dd2d.gunza.data_class.UserStruct

/** 유저 뷰모델*/
class UserViewModel(userId: String): ViewModel() {
    private val userRepo = UserRepository.getInstance(userId)
    private var _userInfo = MutableLiveData<UserStruct>()
    val userInfo: LiveData<UserStruct>
        get() = _userInfo

    init {
        Log.d("LOG_CHECK", "UserViewModel :: () -> init viewModel")
        _userInfo = userRepo.userInfo
    }

    /** * 유저에 데이터 추가
     * @param isRemove true 경우 ,[key] 안 넣어도 됨
     * @see UserRepository.addUserInfo
     * @see UserRepository.removeUserInfo*/
    fun updateUserInfo(field: String, value: Any, fieldType: String, key: String? = null, isRemove: Boolean = false){
        if(isRemove) { userRepo.removeUserInfo(field, value, fieldType) }
        else{ userRepo.addUserInfo(field, value, fieldType, key) }
    }

    /** 로그 아웃 시 호출 [UserRepository.instance] 의 값을 지움
     * @see UserRepository.deleteInstance*/
    fun deleteUserRepo(){
        userRepo.deleteInstance()
    }
}