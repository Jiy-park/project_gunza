package com.example.project_gunza

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.project_gunza.common.*
import com.example.project_gunza.data_class.SignStruct
import com.example.project_gunza.databinding.ActivityMainBinding
import com.example.project_gunza.dialog.DialogSignFunc
import com.example.project_gunza.gunza_info.GunzaInfo
import com.example.project_gunza.main_page.UserViewModel
import com.example.project_gunza.sign.Sign
import com.example.project_gunza.study_room.StudyRoom
import com.example.project_gunza.study_room.StudyRoomAdapter
import com.example.project_gunza.study_room.StudyRoomViewModel

class Main : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userCreateGroupAdapter: StudyRoomAdapter
    private lateinit var context: Context
    private lateinit var pref: Preference

    private lateinit var userViewModel: UserViewModel
    private lateinit var studyRoomViewModel: StudyRoomViewModel
    private lateinit var userId: String

    fun test(){
        binding.testAutoSignIn.apply {
            if(pref.getAutoSignIn()) { this.text = "테스트용 자동 로그인 O"}
            else { this.text = "테스트용 자동 로그인 X" }
            this.setOnClickListener {
                DialogSignFunc.questionAutoSign(context){ answer ->
                    pref.setAutoLogIn(answer)
                    if(answer) { this.text = "테스트용 자동 로그인 O" }
                    else {  this.text = "테스트용 자동 로그인 X" }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        signIn()

        setView()
        setViewEvent()
        setViewModel()

        test()
        binding.recyclerviewMyGroup.apply {
            layoutManager = GridLayoutManager(context, VALUE.NUM_OF_VIEW_GRID)
            adapter = userCreateGroupAdapter
        }
    }

    private fun signIn(){
        userId = intent.getStringExtra(INTENT.SIGN.ID)!!
        val pw = intent.getStringExtra(INTENT.SIGN.PW)!!
        pref.setSign(SignStruct(userId, pw))
    }


    private fun init(){
        binding = DataBindingUtil.setContentView(this@Main, R.layout.activity_main)
        userCreateGroupAdapter = StudyRoomAdapter(isMain = true)
        context = binding.root.context
        pref = Preference(context)
    }

    /** * 뷰모델 설정
     *- 유저 뷰모델 */
    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel(){
        userViewModel = ViewModelProvider(this@Main, ViewModelFactory(userId))[UserViewModel::class.java]
        userViewModel.userInfo.observe(this@Main) { user ->
            pref.setUser(user)
            binding.userViewModel = user
        }

        studyRoomViewModel = StudyRoomViewModel()
        studyRoomViewModel.roomInfo.observe(this@Main){ groupList ->
            userCreateGroupAdapter.studyGroupList = groupList
            userCreateGroupAdapter.notifyDataSetChanged()
        }
    }

    /** * 뷰 설정*/
    private fun setView(){
        binding.layerTopPanel.btnBack.visibility = View.INVISIBLE
        binding.layerTopPanel.btnSetting.visibility = View.VISIBLE
    }

    /** * 뷰 이벤트 설정*/
    private fun setViewEvent(){
        binding.layerTopPanel.btnSetting.setOnClickListener {
//            TODO("임시로 로그아웃 기능만 추가 나중에 더 추가해야함")
            DialogSignFunc.signOut(context){
                userViewModel.deleteUserRepo()
                val intent = Intent(context, Sign::class.java).apply {
                    putExtra(INTENT.SIGN.OUT, true)
                }
                startActivity(intent)
                finish()
            }
        }

        binding.btnMoveStudyGroup.setOnClickListener {
            val intent = Intent(this@Main, StudyRoom::class.java)
            startActivity(intent)
        }

        binding.btnMoveToGunzaInfo.setOnClickListener {
            val intent = Intent(context, GunzaInfo::class.java)
            startActivity(intent)
        }
    }
}