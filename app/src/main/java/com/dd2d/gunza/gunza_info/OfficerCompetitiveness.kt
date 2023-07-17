package com.dd2d.gunza.gunza_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dd2d.gunza.databinding.ActivityOfficerCompetitivenessBinding

class OfficerCompetitiveness : AppCompatActivity() {
    private val binding by lazy { ActivityOfficerCompetitivenessBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.layerTopPanel.btnBack.setOnClickListener { finish() }
    }
}