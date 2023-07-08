package com.example.project_gunza.study_room

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_gunza.common.INTENT
import com.example.project_gunza.common.VALUE
import com.example.project_gunza.data_class.SimpleStudyGroupStruct
import com.example.project_gunza.databinding.CommonGroupViewerBinding
import com.example.project_gunza.study_group.StudyGroup

/** @param isMain 메인 화면에서는 유저 한 명이 만들 수 있는 그룹의 개수 만큼만 보임
 * @see VALUE.CREATE_GROUP_LIMIT*/
class StudyRoomAdapter(private val isMain: Boolean = false): RecyclerView.Adapter<StudyRoomAdapter.Holder>() {
    private lateinit var binding: CommonGroupViewerBinding
    private lateinit var context: Context

    var studyGroupList = listOf<SimpleStudyGroupStruct>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = CommonGroupViewerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = binding.root.context
        return Holder(binding)
    }

    override fun getItemCount() =
        if(isMain) { VALUE.CREATE_GROUP_LIMIT }
        else { studyGroupList.size }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if(studyGroupList.isNotEmpty()) { holder.bind(studyGroupList[position]) }
    }

    inner class Holder(private val binding: CommonGroupViewerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(ssgs: SimpleStudyGroupStruct){
            val groupRank3Unit = getRank3UnitFromGroup(ssgs.memberUnit)
            binding.groupTitle = ssgs.groupTitle
            groupRank3Unit.first?.let { binding.groupRank1Unit = "1위 $it" }
            groupRank3Unit.second?.let { binding.groupRank2Unit = "2위 $it" }
            groupRank3Unit.third?.let { binding.groupRank3Unit = "3위 $it" }
            setViewEvent(ssgs, groupRank3Unit)
        }

        /** * 각 그룹*/
        private fun getRank3UnitFromGroup(memberUnit: Map<String, Int>): Triple<String?, String?, String?>{
            val rankList = memberUnit.toList().sortedByDescending { it.second }
            val rank1 = rankList.getOrNull(0)?.first
            val rank2 = rankList.getOrNull(1)?.first
            val rank3 = rankList.getOrNull(2)?.first
            return Triple(rank1, rank2, rank3)
        }

        /** 스터디 그룹 아이디를 인텐트로 넘겨줌*/
        private fun setViewEvent(ssgs: SimpleStudyGroupStruct, ranking: Triple<String?,String?,String?>){
            itemView.setOnClickListener {
                val intent = Intent(context, StudyGroup::class.java)
                intent.putExtra(INTENT.GROUP.SSGS, ssgs)
                intent.putExtra(INTENT.GROUP.RANK, ranking)
                context.startActivity(intent)
            }
        }
    }
}