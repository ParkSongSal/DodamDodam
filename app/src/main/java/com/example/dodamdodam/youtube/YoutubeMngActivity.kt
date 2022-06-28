package com.example.dodamdodam.youtube

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.dodamdodam.BaseActivity
import com.example.dodamdodam.R
import com.example.dodamdodam.Retrofit2.ResultModel
import com.example.dodamdodam.utils.Common
import kotlinx.android.synthetic.main.activity_youtube_mng.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YoutubeMngActivity : BaseActivity() {

    var youtubeParamV = ""

    lateinit var call: Call<ResultModel>

    private val youtubeLinkEdit: EditText by lazy {
        findViewById<EditText>(R.id.youtubeLinkEdit)
    }
    private val youtubeLinkChk: Button by lazy {
        findViewById<Button>(R.id.youtubeLinkChk)
    }

    private val updateBtn: Button by lazy {
        findViewById<Button>(R.id.updateBtn)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_mng)

        init(applicationContext)

        loginId = setting.getString("loginId", "").toString()

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "유튜브 링크 관리"
        setSupportActionBar(toolbar)

        youtubeLinkChk.setOnClickListener {
            youtubeParamV = youtubeLinkEdit.text.toString()
            if (youtubeParamV == "") {
                Toast.makeText(applicationContext, "적합한 링크가 아닙니다.", Toast.LENGTH_LONG).show()
            } else {
                startActivity(Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://www.youtube.com/watch?v=$youtubeParamV"))
                    .setPackage("com.google.android.youtube"))

            }
        }

        updateBtn.setOnClickListener{
            updateYoutubeLink()
        }
    }

    private fun updateYoutubeLink(){

        val boardGubun = "3"
        val contentText = youtubeLinkEdit.text.toString()
        if (contentText == "") {
            dlg.setMessage("링크를 입력해주세요.")
                .setNegativeButton("확인", null)
            dlg.show()
            return
        }

        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)
        val boardContentPart = RequestBody.create(MultipartBody.FORM, contentText)
        val updateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)

        call = mBoardApi.getIntroduceModify(
            boardGubunPart,
            boardContentPart,
            updateIdPart,
            updateDatePart)

        call.enqueue(object : Callback<ResultModel> {
            override fun onResponse(call: Call<ResultModel>, response: Response<ResultModel>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@YoutubeMngActivity,
                        "수정되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultModel>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@YoutubeMngActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
