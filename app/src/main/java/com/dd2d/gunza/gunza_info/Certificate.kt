package com.dd2d.gunza.gunza_info

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.dd2d.gunza.R
import com.dd2d.gunza.common.INTENT
import com.dd2d.gunza.databinding.ActivityCertificateBinding

/** 자격증 추천
 * @property order 응시순 | 합격순 정하는 값 ]*/
class Certificate : AppCompatActivity() {
    private val binding by lazy { ActivityCertificateBinding.inflate(layoutInflater) }
    private val context by lazy { binding.root.context }
    private var order = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        init()
        setViewEvent()
    }

    private fun init(){
        order = intent.getStringExtra(INTENT.CERTIFICATE.ORDER)?: INTENT.CERTIFICATE.EXAM
        when(order){
            INTENT.CERTIFICATE.EXAM -> {
                Glide.with(context).load(R.drawable.exam_big).into(binding.ivTable)
            }
            INTENT.CERTIFICATE.PASS -> {
                Glide.with(context).load(R.drawable.pass_big).into(binding.ivTable)
            }
        }
    }

    private fun setViewEvent() = with(binding){
        layerTopPanel.btnBack.setOnClickListener { finish() }
        btnExam.setOnClickListener {
            Glide.with(context).load(R.drawable.exam_big).into(ivTable)
        }
        btnPass.setOnClickListener {
            Glide.with(context).load(R.drawable.pass_big).into(ivTable)
        }
        btnMoreData.setOnClickListener {
            val intent = Intent(this@Certificate, CertificateMoreInfo::class.java)
            startActivity(intent)
        }
    }
}