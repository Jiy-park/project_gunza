package com.dd2d.gunza.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dd2d.gunza.common.FIELD
import com.dd2d.gunza.common.INTENT
import com.dd2d.gunza.Main
import com.dd2d.gunza.common.Preference
import com.dd2d.gunza.data_class.SignStruct
import com.dd2d.gunza.data_class.UserStruct
import com.dd2d.gunza.databinding.ActivityStartAppBinding
import com.dd2d.gunza.dialog.DialogSignFunc
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Sign : AppCompatActivity() {
    private val db = Firebase.firestore
    private val binding by lazy { ActivityStartAppBinding.inflate(layoutInflater) }
    private val context by lazy { binding.root.context }
    private val pref by lazy { Preference(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        checkAutoSign()
        setViewEvent()
    }

    /** 자동 로그인 설정이 돼 있을 경우 바로 메인 화면으로 넘어감
     *- 로그 아웃인 경우: isSignOut == true -> 다시 로그인 안됨*/
    private fun checkAutoSign(){
        val isSignOut = intent.getBooleanExtra(INTENT.SIGN.OUT, false)

        if(isSignOut) { return } // 로그아웃으로 접근 -> 다시 로그인 되는 것을 방지

        if(pref.getAutoSignIn()) {
            val id = pref.getUserId()
            val pw = pref.getUserPw()

            moveToMain(id, pw)
        }
    }

    private fun setViewEvent(){
        binding.btnSignUp.setOnClickListener { signUp() }
        binding.btnSignIn.setOnClickListener { signIn() }
    }

    /** 회원 가입
     * @see DialogSignFunc.signUp*/
    private fun signUp(){
        DialogSignFunc.signUp(context) { id: String, name: String, unit: String, pw: String ->
            val newUser = UserStruct(userId = id, userName = name, userUnit = unit)
            val logInInfo = SignStruct(id, pw)

            DialogSignFunc.questionAutoSign(context){ answer->
                db.collection(FIELD.LOGIN.ROOT).document(id).set(logInInfo)
                db.collection(FIELD.USER.ROOT).document(id).set(newUser)
                pref.setAutoLogIn(answer)
                moveToMain(id, pw)
            }
        }
    }

    private fun signIn(){
        val autoId = pref.getUserId()
        val autoPw = pref.getUserPw()

        DialogSignFunc.signIn(context, autoId, autoPw){ id, pw ->
            moveToMain(id, pw)
        }
    }

    /** [Main]으로 이동*/
    private fun moveToMain(id: String, pw: String){
        val intent = Intent(context, com.dd2d.gunza.Main::class.java).apply {
            putExtra(INTENT.SIGN.ID, id)
            putExtra(INTENT.SIGN.PW, pw)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        Log.d("LOG_CHECK", "Sign :: moveToMain() -> sign in user [ID : $id] [PW : $pw]")
        startActivity(intent)
        finish()
    }
}