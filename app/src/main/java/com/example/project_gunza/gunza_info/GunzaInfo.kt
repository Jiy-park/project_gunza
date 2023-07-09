package com.example.project_gunza.gunza_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.project_gunza.common.INTENT
import com.example.project_gunza.databinding.ActivityGunzaInfoBinding

class GunzaInfo : AppCompatActivity() {
    private val binding by lazy { ActivityGunzaInfoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewEvent()
    }

    private fun setViewEvent() = with(binding){
        btnBackToMain.setOnClickListener { finish() }
        examCertificate.setOnClickListener { moveToCertificate(INTENT.CERTIFICATE.EXAM) } // 응시순 자격증 추천
        passCertificate.setOnClickListener { moveToCertificate(INTENT.CERTIFICATE.PASS) } // 합격순 자격증 추천
        remoteCourse.setOnClickListener { moveToCourse() } // 대학변 원격 강의 개설 과목 확인
        remoteCoursePass.setOnClickListener { moveToCoursePass() } // 대학별 원격 강의 수료순 확인
        army.setOnClickListener { moveToOfficerSchool(INTENT.OFFICER.ARMY) } // 육군 사관 학교 교육 과정
        competitiveness.setOnClickListener { moveToOfficerCompetitiveness() } // 각군 사관 학교 입학 경쟁률
    }

    /** 자격증 추천
     * @param order 정렬 순서 ->
     *[EXAM]: 응시순
     *[PASS]: 합격순*/
    private fun moveToCertificate(order: String){
        val intent = Intent(this@GunzaInfo, Certificate::class.java).apply {
            putExtra(INTENT.CERTIFICATE.ORDER, order)
        }
        startActivity(intent)
    }

    /** 대학변 원격 강의 개설 과목 확인*/
    private fun moveToCourse(){
        val intent = Intent(this@GunzaInfo, Course::class.java)
        startActivity(intent)
    }

    /**  // 대학별 원격강의 수료순 확인*/
    private fun moveToCoursePass(){
        val intent = Intent(this@GunzaInfo, CoursePass::class.java)
        startActivity(intent)
    }

    private fun moveToOfficerSchool(detail: String){
        val intent = Intent(this@GunzaInfo, OfficerSchool::class.java).apply {
            putExtra(INTENT.OFFICER.IS, detail)
        }
        startActivity(intent)

    }

    /** 병사 확인 혜택 정보*/
    private fun moveToOfficerCompetitiveness(){
        val intent = Intent(this@GunzaInfo, OfficerCompetitiveness::class.java)
        startActivity(intent)
    }
}