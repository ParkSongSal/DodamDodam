package com.example.dodamdodam

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.dodamdodam.Introduce.AppIntroduceActivity
import com.example.dodamdodam.Introduce.EnterIntroduceActivity
import com.example.dodamdodam.Introduce.OutIntroduceActivity
import com.example.dodamdodam.Notice.NoticeListActivity
import com.example.dodamdodam.Visit.ResultVisit
import com.example.dodamdodam.Visit.VisitAdminUserSelActivity
import com.example.dodamdodam.utils.BackPressedForFinish
import com.example.dodamdodam.utils.Common
import com.example.dodamdodam.youtube.YoutubeMngActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    var backPressedForFinish : BackPressedForFinish? = null

    private val loginTxt: TextView by lazy {
        findViewById<TextView>(R.id.Login_txt)
    }
    private val babyNameTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyNameTxt)
    }
    private val babyBirthDateTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyBirthDateTxt)
    }
    private val babyNumTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyNumTxt)
    }
    private val babyBirthTimeTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyBirthTimeTxt)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init(this@MainActivity)

        loginId = setting.getString("loginId", "").toString()

        getBabyInfo(loginId)

        backPressedForFinish = BackPressedForFinish(this@MainActivity)
    }


    // 아기 정보 조회
    fun getBabyInfo(loginId: String) {
        if(loginId == "admin"){
            loginTxt.text = "메인"
            babyNameTxt.text = "관리자님"
            babyBirthDateTxt.text = "환영합니다."
        }else{
            val loginIdPart = RequestBody.create(MultipartBody.FORM, loginId)

            mUserApi.mainBabyInfo(loginIdPart).enqueue(object :
                Callback<List<ResultVisit>> {
                override fun onResponse(
                    call: Call<List<ResultVisit>>,
                    response: Response<List<ResultVisit>>
                ) {

                    //정상 결과
                    val result: List<ResultVisit>? = response.body()

                    Log.d("TAG", "list : $result")
                    for (i in result!!.indices) {
                        babyNameTxt.text = "● " + result[i].babyName
                        babyNumTxt.text = " " + result[i].babyNum
                        babyBirthDateTxt.text = "● " + Common.dataSplitFormat(
                            result[i].babyBirthDate,
                            "date"
                        )
                        babyBirthTimeTxt.text = Common.dataSplitFormat(
                            result[i].babyBirthTime,
                            "time"
                        )
                    }
                }

                override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
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

    fun mOnClick(view: android.view.View) {

        when (view.id) {

            /* 앱 소개 */
            /*R.id.intro_ll,
            R.id.appIntro,
            R.id.intro_img -> {
                Common.intentCommon(this@MainActivity, AppIntroduceActivity::class.java)
            }*/

            /* 입원 안내문 소개  */

            R.id.enter_ll,
            R.id.enterTxt,
            R.id.enter_img -> {
                Common.intentCommon(this@MainActivity, EnterIntroduceActivity::class.java)
            }

            /* 퇴원 안내문 소개*/
            R.id.leave_ll,
            R.id.leave_txt,
            R.id.leave_img -> {
                Common.intentCommon(this@MainActivity, OutIntroduceActivity::class.java)
            }

            /* TODO : 유튜브 소개*/
            R.id.youtube_ll,
            R.id.youtubeTxt,
            R.id.youtubeImg -> {
                val a = "0_ppWFD1ba0"
                /*TODO : admin 계정 임시 조치 */
                /*TODO : 등록한 Youtube 링크 불러오기 */
                if(loginId != "admin"){
                    startActivity(Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse("https://www.youtube.com/watch?v=$a"))
                        .setPackage("com.google.android.youtube"))
                }else{
                    Common.intentCommon(this@MainActivity, YoutubeMngActivity::class.java)
                }
            }

            /* 설정 */
            R.id.setting_ll,
            R.id.setting_txt,
            R.id.setting_img -> {
                Common.intentCommon(this@MainActivity, SettingActivity::class.java)
            }
            /*
            R.id.logoutImg -> {
                Common.intentCommon(this@MainActivity, SettingActivity::class.java)
                finish()
            }

            */
            /* 공지사항 */

            R.id.notice_ll,
            R.id.notice_txt,
            R.id.notice_img -> {
                Common.intentCommon(this@MainActivity, NoticeListActivity::class.java)
            }
            /* 면회 */
            R.id.visit_ll,
            R.id.visit_txt,
            R.id.visit_img -> {
                /*TODO : admin 계정 임시 조치 */
                if ("admin"!= loginId) {
                    Common.intentCommon(this@MainActivity, VisitAdminUserSelActivity::class.java)
                } else {
                    //Common.intentCommon(this@MainActivity, VisitParentCalendarActivity::class.java)
                }
            }
        }
    }

    // 뒤로가기 종료버튼
    override fun onBackPressed() {
        // BackPressedForFinish 클래시의 onBackPressed() 함수를 호출한다.
        backPressedForFinish?.onBackPressed()
    }
}