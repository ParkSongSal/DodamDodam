package com.example.dodamdodam

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.dodamdodam.Retrofit2.ResultModel
import com.example.dodamdodam.utils.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : BaseActivity() {

    private var passwordOk = true
    private var babyRelation = "D"




    private val userIdEdit: EditText by lazy {
        findViewById<EditText>(R.id.userIdEdit)
    }
    private val passwordEdit: EditText by lazy {
        findViewById<EditText>(R.id.passwordEdit)
    }

    private val password2Edit: EditText by lazy {
        findViewById<EditText>(R.id.password2Edit)
    }

    private val babyRelationRadioGroup: RadioGroup by lazy {
        findViewById<RadioGroup>(R.id.babyRelationRadioGroup)
    }

    private val dadRadio: RadioButton by lazy {
        findViewById<RadioButton>(R.id.dadRadio)
    }

    private val momRadio: RadioButton by lazy {
        findViewById<RadioButton>(R.id.momRadio)
    }
    private val phoneEdit: EditText by lazy {
        findViewById<EditText>(R.id.phoneEdit)
    }
    private val nameEdit: EditText by lazy {
        findViewById<EditText>(R.id.nameEdit)
    }
    private val babyNameEdit: EditText by lazy {
        findViewById<EditText>(R.id.babyNameEdit)
    }
    private val babyNumEdit: EditText by lazy {
        findViewById<EditText>(R.id.babyNumEdit)
    }

    private val babyBirthTime: EditText by lazy {
        findViewById<EditText>(R.id.babyBirthTime)
    }
    private val babyBirthDate: EditText by lazy {
        findViewById<EditText>(R.id.babyBirthDate)
    }
    private val valiBtn: Button by lazy {
        findViewById<Button>(R.id.valiBtn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        init(applicationContext)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "????????????"
        setSupportActionBar(toolbar)

        password2Edit.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                //Toast.makeText(getApplicationContext(), "Focus Lose", Toast.LENGTH_SHORT).show();
                val pass1: String = passwordEdit.text.toString()
                val pass2: String = password2Edit.text.toString()
                if (pass1 == pass2) {
                    passwordOk = true
                    Toast.makeText(applicationContext, "??????????????? ???????????????.", Toast.LENGTH_SHORT).show()
                } else {
                    passwordOk = false
                    Toast.makeText(applicationContext, "??????????????? ???????????? ????????????.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        // ???????????? ?????? ????????? ?????? ?????????
        babyRelationRadioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.dadRadio ->
                    if (dadRadio.isChecked) {
                        babyRelation = "D"
                    }
                R.id.momRadio ->
                    if (momRadio.isChecked) {
                        babyRelation = "M"
                    }
            }
        }

        phoneEdit.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        this.InitializeListener()

        babyBirthDate.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. ????????????
                    val dialog = DatePickerDialog(this, dateCallbackMethod, 2021, 6, 10)

                    dialog.show()
                } else {
                    //  .. ????????? ????????? ???
                }
            }

        babyBirthTime.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    //  .. ????????????
                    val dialog = TimePickerDialog(
                        this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timeCallbackMethod,
                        8,
                        10,
                        false
                    )

                    dialog.show()
                } else {
                    //  .. ????????? ????????? ???
                }
            }
    }

    fun InitializeListener() {
        dateCallbackMethod =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val month = when (monthOfYear + 1) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> monthOfYear.toString()
                }
                val day = when (dayOfMonth) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> dayOfMonth.toString()
                }

                babyBirthDate.setText(
                    "$year-$month-$day"
                )
            }

        timeCallbackMethod =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val hour = when (hourOfDay) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> hourOfDay.toString()
                }
                val min = when (minute) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> minute.toString()
                }
                babyBirthTime.setText("$hour:$min")
            }
    }

    // ID ????????????
    private fun validateAct() {
        val userId = userIdEdit.text.toString()

        if (userId == "") {
            dlg.setMessage("ID??? ??????????????????.")
                .setNegativeButton("??????", null)
            dlg.show()
            return
        }

        mUserApi.userIdValidate(userId).enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                // ????????????
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@RegisterActivity,
                        "?????? ????????? ID ?????????.",
                        Toast.LENGTH_SHORT
                    ).show()
                    userIdEdit.isEnabled = false
                    validate = true
                    valiBtn.setBackgroundColor(resources.getColor(R.color.mainTextColor))
                    valiBtn.setTextColor(resources.getColor(R.color.white))
                } else {
                    //????????? ????????? ??????
                    dlg.setMessage("?????? ???????????? ID ?????????.")
                        .setNegativeButton("??????", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                Log.d("TAG", "???????????? Failed ${t.message}")
                // ???????????? ??????
                Toast.makeText(
                    this@RegisterActivity,
                    "????????? ?????? ????????? ?????? ??? ?????? ??????????????????.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    // ????????????
    private fun registerAct() {
        val userId = userIdEdit.text.toString()
        val userPw = passwordEdit.text.toString()
        val userName = nameEdit.text.toString()
        val userPhone = phoneEdit.text.toString()
        val babyName = babyNameEdit.text.toString()
        val babyNum = babyNumEdit.text.toString()
        val babyBirthDate = babyBirthDate.text.toString()
        val babyBirthTime = babyBirthTime.text.toString()
        val regDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
        var userAuth = "U"
        if ("admin" == userId) {
            userAuth = "A"
        }

        if (!validate) {
            dlg.setMessage("?????? ?????? ????????? ????????????.")
                .setNegativeButton("??????", null)
            dlg.show()
            return
        }

        if (userId == "" || userPw == "" || userName == "" || userPhone == "" || babyName == "" || babyNum == "") {
            dlg.setMessage("??? ??? ?????? ??????????????????.")
                .setNegativeButton("??????", null)
            dlg.show()
            return
        }
        if (!passwordOk) {
            dlg.setMessage("???????????? ??????????????? ???????????? ????????????.")
                .setNegativeButton("??????", null)
            dlg.show()
            return
        }

        mUserApi.userRegister(
            userId,
            userPw,
            userName,
            userPhone,
            babyName,
            babyNum,
            babyBirthDate,
            babyBirthTime,
            babyRelation,
            regDate,
            userAuth
        ).enqueue(object :
            Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                //?????? ??????
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@RegisterActivity,
                        "$userId ??? ????????? ??????????????????!",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    //????????? ????????? ??????
                    dlg.setMessage("????????? ????????? ??????????????????.\n ?????? ?????? ??????????????????.")
                        .setNegativeButton("??????", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                // ???????????? ??????
                Toast.makeText(
                    this@RegisterActivity,
                    "????????? ?????? ????????? ?????? ??? ?????? ??????????????????.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }
    fun onClick(view: android.view.View) {
        when (view.id) {
            R.id.valiBtn -> validateAct()
            R.id.registerBtn -> registerAct()
            R.id.cancelBtn -> finish()
        }
    }
}