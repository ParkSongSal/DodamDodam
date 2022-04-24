package com.example.dodamdodam

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.dodamdodam.Retrofit2.RetrofitClient
import com.example.dodamdodam.Retrofit2.userApi
import com.example.dodamdodam.utils.CustomProgressDialog
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    lateinit var coxt : Context

    lateinit var progressDialog : CustomProgressDialog
    val handler = Handler()

    // 아이디 저장 기능
    lateinit var setting : SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var retrofit: Retrofit
    lateinit var mUserApi: userApi

    fun init(context: Context) {

        coxt = context
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        progressDialog = CustomProgressDialog(context)

        // SharedPreferences Init
        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()

        retrofit = RetrofitClient.getInstance()
        mUserApi = retrofit.create(userApi::class.java)

    }
}