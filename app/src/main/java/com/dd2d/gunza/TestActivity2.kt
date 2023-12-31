package com.dd2d.gunza

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.dd2d.gunza.common.Preference
import com.dd2d.gunza.common.ViewModelFactory
import com.dd2d.gunza.databinding.ActivityTest2Binding

class TestActivity2 : AppCompatActivity() {
    private val binding by lazy { ActivityTest2Binding.inflate(layoutInflater) }
    private lateinit var testViewModel: TestViewModel
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val userId = Preference(binding.root.context).getUserId()

        testViewModel = ViewModelProvider(this@TestActivity2, ViewModelFactory(userId))[TestViewModel::class.java]

        Log.d("LOG_CHECK", "TestActivity :: onCreate() -> ${testViewModel.userInfo.value}")
    }
}