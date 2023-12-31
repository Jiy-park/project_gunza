package com.dd2d.gunza

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.dd2d.gunza.databinding.ActivityTestBinding
import com.dd2d.gunza.common.Preference
import com.dd2d.gunza.common.ViewModelFactory

class TestActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    private lateinit var testViewModel: TestViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userId = Preference(binding.root.context).getUserId()

        testViewModel = ViewModelProvider(this@TestActivity, ViewModelFactory(userId))[TestViewModel::class.java]
        Log.d("LOG_CHECK", "TestActivity :: onCreate() -> ${testViewModel.userInfo.value}")

        binding.btn.setOnClickListener {
            val intent = Intent(this@TestActivity, TestActivity2::class.java)
            startActivity(intent)
        }
    }
}