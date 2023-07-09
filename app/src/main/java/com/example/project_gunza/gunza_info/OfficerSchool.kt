package com.example.project_gunza.gunza_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.example.project_gunza.R
import com.example.project_gunza.common.INTENT
import com.example.project_gunza.common.INTENT.OFFICER.AIR
import com.example.project_gunza.common.INTENT.OFFICER.ARMY
import com.example.project_gunza.common.INTENT.OFFICER.NAVY
import com.example.project_gunza.common.INTENT.OFFICER.ROTC
import com.example.project_gunza.databinding.ActivityOfficerSchoolBinding

class OfficerSchool : AppCompatActivity() {
    private val binding by lazy { ActivityOfficerSchoolBinding.inflate(layoutInflater) }
    private var officerSchool = ARMY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getDataFromIntent()
        setViewEvent()
    }

    private fun getDataFromIntent(){
        officerSchool = intent.getStringExtra(INTENT.OFFICER.IS)?: ARMY
        binding.officer = officerSchool
        binding.root.invalidate()
    }

    private fun setViewEvent(){
        binding.layerTopPanel.btnBack.setOnClickListener { finish() }
        binding.btnArmy.setOnClickListener { changeOfficerImage(it) }
        binding.btnAir.setOnClickListener { changeOfficerImage(it) }
        binding.btnNavy.setOnClickListener { changeOfficerImage(it) }
        binding.btnROTC.setOnClickListener { changeOfficerImage(it) }
    }

    private fun changeOfficerImage(v: View){
        with(INTENT.OFFICER){
            when(v){
                binding.btnArmy -> { officerSchool = ARMY }
                binding.btnAir -> { officerSchool = AIR }
                binding.btnNavy -> { officerSchool = NAVY }
                binding.btnROTC -> { officerSchool = ROTC }
            }
            binding.officer = officerSchool
        }
    }
}