package com.example.project_gunza.study_group

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_gunza.*
import com.example.project_gunza.common.*
import com.example.project_gunza.data_class.PostStruct
import com.example.project_gunza.data_class.SimpleStudyGroupStruct
import com.example.project_gunza.databinding.ActivityStudyGroupBinding
import com.example.project_gunza.dialog.DialogFunc
import com.example.project_gunza.main_page.UserViewModel

class StudyGroup : AppCompatActivity() {
    private lateinit var binding: ActivityStudyGroupBinding
    private lateinit var context: Context
    private lateinit var pref: Preference
    private lateinit var studyGroupPostAdapter: StudyGroupPostAdapter

    private lateinit var userViewModel: UserViewModel
    private lateinit var studyGroupViewModel: StudyGroupViewModel

    private var groupInfo: SimpleStudyGroupStruct? = null
    private lateinit var rankingTop3: Triple<String?,String?,String?>

    /** 게시글 작성 완료 콜백.
     *- 작성 완료 시 새로 고침, 그룹에 가입*/
    private var createCallbackLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                Log.d("LOG_CHECK", "StudyGroup :: () -> callback")
                val post =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        result.data?.getParcelableExtra(INTENT.POST.CREATE_POST, PostStruct::class.java)
                    }
                    else {
                        result.data?.getParcelableExtra(INTENT.POST.CREATE_POST)
                    }
                studyGroupViewModel.createPost(post!!){
                    userViewModel.updateUserInfo(FIELD.USER.POST, post.postId, FIELD.TYPE.LIST)
                    userViewModel.updateUserInfo(FIELD.USER.EXP, VALUE.EXP_OF_ONE_POST, FIELD.TYPE.NORMAL)
                    studyGroupViewModel.updateGroupInfo(post.groupId, FIELD.GROUP.POST_ID_LIST, post.postId, FIELD.TYPE.LIST)
                    Toast.makeText(context, "게시글 작성을 완료 했습니다.", Toast.LENGTH_SHORT).show()

                }
                studyGroupViewModel.refresh()
                if(post.authorId !in (groupInfo?.groupMemberId ?: emptyList())){
                    joinStudyGroup()
                }
            }
        }

    override fun onRestart() {
        super.onRestart()
        Log.d("LOG_CHECK", "StudyGroup :: onRestart() -> ")
        studyGroupViewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()

        setViewEvent()
        getSimpleStudyGroupData()
        setViewModel(groupInfo?.groupId?: EXCEPTION.NOT_FOUND_GROUP_ID)
        setUserVisit()

        binding.recyclerviewStudyGroupPost.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = studyGroupPostAdapter
        }
    }

    private fun init(){
        binding = DataBindingUtil.setContentView(this@StudyGroup, R.layout.activity_study_group)
        context = binding.root.context
        pref = Preference(context)
        studyGroupPostAdapter = StudyGroupPostAdapter()
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun setViewModel(groupId: String){
        userViewModel = ViewModelProvider(this@StudyGroup, ViewModelFactory(Preference(context).getUserId()))[UserViewModel::class.java]

        studyGroupViewModel = ViewModelProvider(this@StudyGroup, ViewModelFactory(groupId))[StudyGroupViewModel::class.java]
        studyGroupViewModel.groupInfo.observe(this@StudyGroup){ postList ->
            studyGroupPostAdapter.postList = postList
            studyGroupPostAdapter.authorList = studyGroupViewModel.authorInfo
            studyGroupPostAdapter.notifyDataSetChanged()
        }
    }

    private fun setViewEvent(){
        binding.btnSearch.setOnClickListener { searchPost() }
        binding.btnCreate.setOnClickListener { createPost() }
        binding.btnMyPost.setOnClickListener { viewMyPost() }
        binding.btnBackToMain.setOnClickListener { backToMain(it) }
    }

    /** 검색 그룹 보기 -> 모든 그룹 보기 전환*/
    @SuppressLint("NotifyDataSetChanged")
    private fun backToMain(view: View){
        studyGroupViewModel.defaultValue()
        studyGroupPostAdapter.notifyDataSetChanged()
        view.visibility = View.GONE
        binding.tvGroupTitle.text = "스터디룸"
    }

    /** 그룹 내 게시글 검색 */
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun searchPost(){
        DialogFunc.searchDialog(context, VALUE.SEARCH_GROUP_POST){ searchTitle ->
            studyGroupViewModel.searchPostByTitle(searchTitle) {
                binding.tvGroupTitle.text = "\"$searchTitle\" 검색 결과"
                binding.btnBackToMain.visibility = View.VISIBLE
            }
        }
    }

    /** 그룹에 게시글 작성
     * @see createCallbackLauncher
     * @see joinStudyGroup*/
    private fun createPost(){
        val intent = Intent(this@StudyGroup, CreateStudyGroupPost::class.java)
        intent.putExtra(INTENT.GROUP.ID, groupInfo?.groupId)
        createCallbackLauncher.launch(intent)
    }

    /** 그룹 내에서 처음 게시글 작성 시 실행. 해당 그룹에 가입
     * 1. 그룹에 유저 추가
     * 2. 그룹 내 소속 최신화*/
    private fun joinStudyGroup(){
        studyGroupViewModel.updateGroupInfo(groupInfo!!.groupId, FIELD.GROUP.MEMBER, pref.getUserId(), FIELD.TYPE.LIST)
        studyGroupViewModel.updateGroupInfo(groupInfo!!.groupId, FIELD.GROUP.UNIT, 1L, FIELD.TYPE.MAP, pref.getUserUnit())
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun viewMyPost(){
        studyGroupViewModel.viewMyPost(Preference(context).getUserId()){
            binding.tvGroupTitle.text = "내글 보기"
            binding.btnBackToMain.visibility = View.VISIBLE
        }
    }

    /** * 인텐트로 그룹 데이터 받아옴 -> 그룹 명, 그룹 아이디, 그룹 랭킹*/
    private fun getSimpleStudyGroupData(){
        groupInfo =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getParcelableExtra(INTENT.GROUP.SSGS, SimpleStudyGroupStruct::class.java)
            }
            else { intent.getParcelableExtra(INTENT.GROUP.SSGS)  }

        groupInfo?.let {
            binding.simple = groupInfo
            rankingTop3 =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getSerializableExtra(INTENT.GROUP.RANK, Triple::class.java) as Triple<String?, String?, String?> }
                else{
                    (intent.getSerializableExtra(INTENT.GROUP.RANK)) as Triple<String?, String?, String?>
                }
            rankingTop3.first?.let { binding.groupRank1Unit = it }
            rankingTop3.second?.let { binding.groupRank2Unit = it }
            rankingTop3.third?.let { binding.groupRank3Unit = it }
        }?:run { groupNotFound() }
    }

    /** 유저가 그룹을 방문한 시간을 기록*/
    private fun setUserVisit(){
        userViewModel
            .updateUserInfo(
                FIELD.USER.VISIT,
                System.currentTimeMillis(),
                FIELD.TYPE.MAP,
                key = groupInfo?.groupId
            )
    }

    /** * 스터디 그룹을 찾기 못함 : 인텐트로 넘어오지 않음*/
    private fun groupNotFound(){
        Toast.makeText(context, "그룹을 찾을 수 없습니다", Toast.LENGTH_SHORT).show()
        finish()
    }
}