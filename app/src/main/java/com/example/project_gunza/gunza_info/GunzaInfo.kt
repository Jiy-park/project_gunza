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
        remoteCourseSubjects.setOnClickListener { moveToCourse() } // 대학변 원격 강의 개설 과목 확인
        defenseLibraryBooks.setOnClickListener { moveToLibrary() } // 국방 전자 도서관 도서 정보
        militaryResorts.setOnClickListener { moveToResort() } // 군 복지 휴양 시설
        recreationalNearbyMines.setOnClickListener { moveToRecreational() } // 휴양 시설 주변 관광지
        soldierBenefits.setOnClickListener { moveToBenefit() } // 병사 확인 혜택 정보
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

    /**  국방 전자 도서관 도서 정보*/
    private fun moveToLibrary(){
        val intent = Intent(this@GunzaInfo, Course::class.java)
        startActivity(intent)
    }

    /** 군 복지 휴양 시설*/
    private fun moveToResort(){

    }

    /** 휴양 시설 주변 관광지*/
    private fun moveToRecreational(){

    }

    /** 병사 확인 혜택 정보*/
    private fun moveToBenefit(){

    }
}