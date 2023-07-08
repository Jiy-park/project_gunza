package com.example.project_gunza.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import com.example.project_gunza.R
import com.example.project_gunza.common.FIELD
import com.example.project_gunza.data_class.SignStruct
import com.example.project_gunza.databinding.DialogSignInBinding
import com.example.project_gunza.databinding.DialogSignUpBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DialogSignFunc {
    companion object{
        /** 로그 아웃*/
        fun signOut(context: Context, callback: () -> Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_out, null)

            with(dialog){

                setLeftBtnClickListener {
                    callback()
                    dismiss()
                }
                this.view = view
                initDialog()
            }
        }


        /** 로그인
         * @param callback 로그인 성공, 메인 페이지로 이동*/
        fun signIn(context: Context, autoId: String = "", autoPw: String = "", callback: (id: String, pw: String)->Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_in, null)
            val binding = DialogSignInBinding.bind(view)
            val db = Firebase.firestore.collection(FIELD.LOGIN.ROOT)
            with(dialog){
                title = "로그인"
                leftBtnName = "확인"
                rightBtnName = "취소"

                binding.editId.setText(autoId)
                binding.editPw.setText(autoPw)

                setLeftBtnClickListener {
                    with(binding){
                        val id = editId.text.toString()
                        val pw = editPw.text.toString()

                        if(id.isEmpty()){ // 아이디 입력 안함
                            warnIdEmpty.visibility = View.VISIBLE
                            editId.requestFocus()
                        }else{ // 아이디 입력 함
                            warnIdEmpty.visibility = View.GONE

                            db.document(id).get().addOnSuccessListener {
                                it.toObject(SignStruct::class.java)?.let { info ->
                                    if(info.id == id && info.pw == pw) {
                                        callback(id, pw)
                                        dismiss()
                                    }else{ // 회원 정보 일치 X
                                        warnInfoCheck.visibility = View.VISIBLE
                                    }
                                }?: run{// 입력된 값에 해당하는 정보 없음
                                    warnInfoCheck.visibility = View.VISIBLE
                                }
                            }
                        }

                    }
                }
                this.view = view
                initDialog()
            }
        }

        /** 자동 로그인 사용 여부 확인*/
        fun questionAutoSign(context: Context, callback: (answer: Boolean)->Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_auto_sign_in, null)

            with(dialog){
                rightBtnName = "아니오"
                leftBtnName = "예"
                setLeftBtnClickListener {
                    callback(true)
                    dismiss()
                }
                setRightBtnClickListener {
                    callback(false)
                    dismiss()
                }
                this.view = view
                initDialog()
            }
        }

        /** 회원가입
         * @param checkId 아이디 중복 체크 -> 중복 아닐 시 true 받음
         * @param conditionOk 회원 가입 조건 완료 */
        fun signUp(context: Context, conditionOk: (id: String, name: String, unit: String, pw: String)->Unit){
            val dialog = DialogFrame(context)
            val view = LayoutInflater.from(context).inflate(R.layout.dialog_sign_up, null)
            val binding = DialogSignUpBinding.bind(view)

            with(dialog){
                title = "회원 가입"
                leftBtnName = "취소"
                rightBtnName = "가입완료"

                setRightBtnClickListener{
                    with(binding){
                        val id = editId.text.toString()
                        val name = editName.text.toString()
                        val unit = editUnit.text.toString()
                        val pw = editPw.text.toString()
                        val db = Firebase.firestore.collection(FIELD.LOGIN.ROOT)

                        if(editId.text.isEmpty()){ // 아이디 입력 안함
                            warnIdEmpty.visibility = View.VISIBLE
                            editId.requestFocus()
                        }else{ // 아이디 입력 함
                            warnIdEmpty.visibility = View.GONE

                            db.whereEqualTo(FIELD.LOGIN.ID, id).get().addOnSuccessListener { res -> // 아이디 중복 체크
                                if(!res.isEmpty){ // 아이디 중복
                                    warnIdCheck.visibility = View.VISIBLE
                                    editId.backgroundTintList = ContextCompat.getColorStateList(context, R.color.colorWarnRed)
                                }else{ // 아이디 중복 아님
                                    warnIdCheck.visibility = View.GONE
                                    editId.backgroundTintList = ContextCompat.getColorStateList(context, R.color.black)

                                    if(emptyCheck(binding) && pwCheck(binding)){ // 값 입력 체크 && 비밀번호 체크
                                        conditionOk(id, name, unit, pw)
                                        dismiss()
                                    }
                                }
                            }

                        }
                    }
                }
                this.view = view
                initDialog()
            }
        }

        /** 회원가입 시 입력 값 체크
         *- signUp에 의해 호출
         * @see signUp*/
        private fun emptyCheck(binding: DialogSignUpBinding): Boolean {
            val fields = listOf(
                binding.editName to binding.warnNameEmpty,
                binding.editUnit to binding.warnUnitEmpty,
                binding.editPw to binding.warnPwEmpty
            )

            for ((editText, warningView) in fields) {
                val text = editText.text.toString()
                if (text.isEmpty()) {
                    warningView.visibility = View.VISIBLE
                    editText.requestFocus()
                    return false
                }
                else { warningView.visibility = View.GONE }
            }

            return true
        }

        /** 회원 가입 시 비밀 번호 체크
         *- signUp 에 의해 호출
         * @see signUp */
        private fun pwCheck(binding: DialogSignUpBinding): Boolean{
            val pw1 = binding.editPw.text.toString()
            val pw2 = binding.editPwCheck.text.toString()

            return if(pw1 == pw2){
                binding.warnPwCheck.visibility = View.GONE
                true
            }else{
                binding.warnPwCheck.visibility = View.VISIBLE
                false
            }
        }
    }
}