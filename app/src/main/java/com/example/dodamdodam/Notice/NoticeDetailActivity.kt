package com.example.dodamdodam.Notice

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.dodamdodam.BaseActivity
import com.example.dodamdodam.MainActivity
import com.example.dodamdodam.R
import com.example.dodamdodam.utils.Common
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeDetailActivity : BaseActivity() {

    lateinit var call: Call<ResultNotice>

    var seq = 0
    var title : String?  = ""
    var writer : String? = ""
    var content : String? = ""
    var insertDate : String? = ""

    private val deleteBtn: Button by lazy {
        findViewById<Button>(R.id.deleteBtn)
    }
    private val updateBtn: Button by lazy {
        findViewById<Button>(R.id.updateBtn)
    }
    private val btnLl: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.btnLl)
    }

    private val titleTxt: TextView by lazy {
        findViewById<TextView>(R.id.titleTxt)
    }

    private val userTxt: TextView by lazy {
        findViewById<TextView>(R.id.userTxt)
    }

    private val contentTxt: TextView by lazy {
        findViewById<TextView>(R.id.contentTxt)
    }

    private val dateTxt: TextView by lazy {
        findViewById<TextView>(R.id.dateTxt)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_detail)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "공지사항 상세"
        setSupportActionBar(toolbar)
        init(this@NoticeDetailActivity)

        loginId = setting.getString("loginId", "").toString()

        if (intent != null) {
            seq = intent.getIntExtra("Seq", 0) //글번호
            title = intent.getStringExtra("TITLE")
            writer = intent.getStringExtra("WRITER")
            content = intent.getStringExtra("CONTENT")
            insertDate = intent.getStringExtra("DATE")

            titleTxt.text = title.toString()
            userTxt.text = writer.toString()
            contentTxt.text = content.toString()
            dateTxt.text = insertDate.toString()

            // 관리자만 수정, 삭제 가능
            if (loginId == "admin") {
                btnLl.visibility = View.VISIBLE

                // 수정 버튼
                updateBtn.setOnClickListener{
                    //intent = Intent(this@NoticeDetailActivity, NoticeUpdateActivity::class.java)
                    intent.putExtra("updateSeq",seq)
                    intent.putExtra("updateTitle",title)
                    intent.putExtra("updateContent",content)
                    startActivity(intent)
                }
                // 삭제버튼
                deleteBtn.setOnClickListener{
                    dlg.setTitle("삭제 알림")
                        .setMessage("공지사항을 삭제 하시겠습니까?")
                        .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                            NoticeDeleteAct()
                        })
                        .setNegativeButton("아니오",null)
                    dlg.show()
                }
            } else {
                btnLl.visibility = View.GONE
            }

        } else {
            Common.intentCommon(this@NoticeDetailActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // 공지사항 삭제 로직
    private fun NoticeDeleteAct(){

        val noticeSeqPart = RequestBody.create(MultipartBody.FORM, seq.toString())

        call = mBoardApi.NoticeDelete(noticeSeqPart)
        call.enqueue(object : Callback<ResultNotice> {

            override fun onResponse(call: Call<ResultNotice>, response: Response<ResultNotice>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(this@NoticeDetailActivity, NoticeListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@NoticeDetailActivity,
                        "삭제되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultNotice>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@NoticeDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    //뒤로가기 종료버튼
    override fun onBackPressed() {
        startActivity(Intent(this@NoticeDetailActivity, NoticeListActivity::class.java))
        finish()
    }
}