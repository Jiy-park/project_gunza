package com.dd2d.gunza.common

import android.content.Context
import com.dd2d.gunza.data_class.SignStruct
import com.dd2d.gunza.data_class.UserStruct

class Preference(context: Context) {
    private val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    fun getAutoSignIn() = sharedPref.getBoolean(AUTO_LOGIN, false)
    fun getUserId() = sharedPref.getString(USER_ID, "")?: ""
    fun getUserPw() = sharedPref.getString(USER_PW, "")?: ""
    fun getUserName() = sharedPref.getString(USER_NAME, "")?: ""
    fun getUserUnit() = sharedPref.getString(USER_UNIT,"")?: ""
    fun getUserExp() = sharedPref.getInt(USER_EXP, 0)

    fun setSign(signInfo: SignStruct){
        sharedPref.edit().apply {
            putString(USER_ID, signInfo.id)
            putString(USER_PW, signInfo.pw)
            apply()
        }
    }

    fun setUser(user: UserStruct){
        sharedPref.edit().apply {
            putString(USER_NAME, user.userName)
            putString(USER_UNIT, user.userUnit)
            apply()
        }
    }

    fun setAutoLogIn(answer: Boolean){
        sharedPref.edit().apply {
            putBoolean(AUTO_LOGIN, answer)
            apply()
        }
    }

    companion object KEYS{
        const val PREFERENCE_NAME = "preference"

        const val USER_ID = "userId"
        const val USER_PW = "userPw"
        const val USER_NAME = "userName"
        const val USER_UNIT = "userUnit"
        const val USER_EXP = "userExp"

        const val AUTO_LOGIN = "autoLogIn"
    }
}