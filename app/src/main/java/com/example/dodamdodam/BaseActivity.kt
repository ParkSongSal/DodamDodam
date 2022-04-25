package com.example.dodamdodam

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dodamdodam.Retrofit2.RetrofitClient
import com.example.dodamdodam.Retrofit2.userApi
import com.example.dodamdodam.utils.CustomProgressDialog
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    var validate = false
    var loginId = ""

    lateinit var coxt : Context

    lateinit var progressDialog : CustomProgressDialog
    val handler = Handler()

    var dateCallbackMethod: DatePickerDialog.OnDateSetListener? = null
    var timeCallbackMethod: TimePickerDialog.OnTimeSetListener? = null
    lateinit var dlg: AlertDialog.Builder

    // 아이디 저장 기능
    lateinit var setting : SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    lateinit var retrofit: Retrofit
    lateinit var mUserApi: userApi

    fun init(context: Context) {

        coxt = context
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        progressDialog = CustomProgressDialog(context)
        // AlertDialog Init
        dlg = AlertDialog.Builder(
            context,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )

        // SharedPreferences Init
        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()

        retrofit = RetrofitClient.getInstance()
        mUserApi = retrofit.create(userApi::class.java)

    }
}