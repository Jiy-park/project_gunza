package com.example.project_gunza.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.example.project_gunza.databinding.DialogFrameBinding

class DialogFrame(context: Context): Dialog(context) {
    private val binding by lazy { DialogFrameBinding.inflate(layoutInflater) }
    private var customizingLeft = false
    private var customizingRight = false

    var title = "GUNZA"
    var leftBtnName = "확인"
    var rightBtnName = "취소"
    lateinit var view: View

    fun initDialog(){
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        binding.dialogTitle.text = title
        binding.btnLeft.text = leftBtnName
        binding.btnRight.text = rightBtnName

        if(!customizingLeft) { binding.btnLeft.setOnClickListener { dismiss() } }
        if(!customizingRight) { binding.btnRight.setOnClickListener { dismiss() } }

        binding.layerDialogMainContent.addView(view)
        show()
    }

    fun setLeftBtnClickListener(listener:()->Unit){
        customizingLeft = true
        binding.btnLeft.setOnClickListener {
            listener()
        }
    }

    fun setRightBtnClickListener(listener:()->Unit){
        customizingRight = true
        binding.btnRight.setOnClickListener {
            listener()
        }
    }
}