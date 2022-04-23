package com.example.dodamdodam

import android.content.Context
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    lateinit var coxt : Context


    fun init(context: Context) {

        coxt = context
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

    }
}