package com.dd2d.gunza.gunza_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dd2d.gunza.databinding.ActivityCertificateMoreInfoBinding

class CertificateMoreInfo : AppCompatActivity() {
    private val binding by lazy { ActivityCertificateMoreInfoBinding.inflate(layoutInflater) }
    private val context by lazy { binding.root.context}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewEvent()
    }

    private fun setViewEvent() = with(binding){
        layerTopPanel.btnBack.setOnClickListener { finish() }
    }
}