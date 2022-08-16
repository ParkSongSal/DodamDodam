package com.example.dodamdodam

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.dodamdodam.Retrofit2.*
import com.example.dodamdodam.utils.CustomProgressDialog
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity() {

    lateinit var packageInfo : PackageInfo
    var validate = false
    var loginId = ""
    var actGubun : String? = ""
    var boardGubun: String? = ""
    var introContent : String? = ""

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
    lateinit var mBoardApi: boardApi
    lateinit var mVisitApi: visitApi

    val toolbar: Toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }

    val modifyBtn: Button by lazy {
        findViewById<Button>(R.id.modifyBtn)
    }
    val appIntroTxt: TextView by lazy {
        findViewById<TextView>(R.id.appIntroTxt)
    }


    val outIntroTxt: TextView by lazy {
        findViewById<TextView>(R.id.outIntroTxt)
    }
    fun init(context: Context) {

        packageInfo = applicationContext.packageManager.getPackageInfo(applicationContext.packageName, 0)

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
        mBoardApi = retrofit.create(boardApi::class.java)
        mVisitApi = retrofit.create(visitApi::class.java)

    }


    /* 앱소개
   * 입원 안내문
   * 퇴원 안내문
   * 등록 / 수정 체크
   * */
    fun introduceValidate(boardGubun: String) {

        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)


        mBoardApi.getIntroduceValidate(boardGubunPart).enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                Log.d("TAG", "response : ${response.body()}")

                // 정상결과
                if (response.body()!!.result == "insert") {
                    actGubun = "insert"
                    validate = true
                } else {
                    actGubun = "update"
                    validate = true
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                validate = false
                // 네트워크 문제
                Toast.makeText(
                    coxt,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    /* 앱소개
    * 입원 안내문
    * 퇴원 안내문
    * 등록 내용 셋팅
    * */
    fun introduceList(boardGubun: String){
        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)

        mBoardApi.getIntroduceList(boardGubunPart).enqueue(object :
            Callback<List<ResultIntroduce>> {
            override fun onResponse(
                call: Call<List<ResultIntroduce>>,
                response: Response<List<ResultIntroduce>>
            ) {


                //정상 결과
                val result: List<ResultIntroduce>? = response.body()

                Log.d("TAG", "list : $result")
                for (i in result!!.indices) {
                    val boardGb: String = result[i].boardGubun

                    when (boardGb) {
                        "0" -> appIntroTxt.text = result[i].boardContent
                        //"1" -> enterIntroTxt.text = result[i].boardContent
                       // "2" -> outIntroTxt.text = result[i].boardContent
                        "3" ->  {
                            val youtubeLink = result[i].boardContent
                            editor.putString("youtubeLink", youtubeLink)
                            editor.apply()
                        }

                    }
                }

            }

            override fun onFailure(call: Call<List<ResultIntroduce>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    coxt,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}