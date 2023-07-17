package com.dd2d.gunza.gunza_info

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dd2d.gunza.databinding.ActivityLibraryBinding

class Library : AppCompatActivity() {
    private val binding by lazy { ActivityLibraryBinding.inflate(layoutInflater) }
    private val context by lazy { binding.root.context }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setViewEvent()
    }

    private fun setViewEvent() = with(binding){
        layerTopPanel.btnBack.setOnClickListener { finish() }
        btnSearch.setOnClickListener { searchBook() }
    }

    private fun searchBook(){
        Toast.makeText(context, "책을 찾는다..", Toast.LENGTH_SHORT).show()
    }

}