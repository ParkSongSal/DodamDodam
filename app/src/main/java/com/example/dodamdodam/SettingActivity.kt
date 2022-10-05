package com.example.dodamdodam

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.example.dodamdodam.Introduce.AppIntroduceActivity
import com.example.dodamdodam.Retrofit2.ResultModel
import com.example.dodamdodam.utils.Common
import kotlinx.android.synthetic.main.activity_setting.*
import retrofit2.Callback
import retrofit2.Response

class SettingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "설정"
        setSupportActionBar(toolbar)

        init(this@SettingActivity)

        loginId = setting.getString("loginId", "").toString()

        val appVersion = packageInfo.versionName

        versionTxt.text = "버전 : $appVersion"
    }

    fun mOnClick(view: View) {
        when (view.id) {

            // 앱소개
            R.id.introduceBtn -> {
                Common.intentCommon(this@SettingActivity, AppIntroduceActivity::class.java)
            }

            // 개인정보 처리방침
            R.id.privacyBtn -> {
                //intent = Intent(this@SettingActivity, privacyDetailActivity::class.java)
                intent.putExtra("title", "개인정보처리방침")
                intent.putExtra("fileNm", "개인정보처리방침.html")
                startActivity(intent)
            }

            // 회원정보 수정
            R.id.userUpdateBtn -> {
                userInfoUpdate();
            }

            // 로그아웃
            R.id.logoutBtn -> {
                dlg.setTitle("로그아웃 알림")
                    .setMessage("로그아웃 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        Logout()
                        Toast.makeText(applicationContext, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton(
                        "아니오",
                        null
                    )
                dlg.show()
            }

            R.id.unRegisterBtn -> {
                dlg.setTitle("회원탈퇴 알림")
                    .setMessage("탈퇴시 회원님이 작성한 모든 게시물과\n회원정보가 삭제됩니다.\n탈퇴를 진행하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        if ("admin" == loginId) {
                            Toast.makeText(
                                applicationContext,
                                "관리자 계정은 탈퇴할수 없습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            progressDialog.show()
                            Handler().postDelayed(Runnable {
                                UnRegister()
                                //여기에 딜레이 후 시작할 작업들을 입력
                            }, 2000) // 0.5초 정도 딜레이를 준 후 시작

                        }
                    })
                    .setNegativeButton(
                        "아니오",
                        null
                    )
                dlg.show()

            }

            // 개발자에게 문의하기
            R.id.contact_us -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "plain/text"
                // email setting 배열로 해놔서 복수 발송 가능
                // email setting 배열로 해놔서 복수 발송 가능
                val address = arrayOf("psmapp0102@naver.com")
                intent.putExtra(Intent.EXTRA_EMAIL, address)
                intent.putExtra(Intent.EXTRA_SUBJECT, "")
                intent.putExtra(Intent.EXTRA_TEXT, "")
                startActivity(intent)
            }
        }
    }

    private fun userInfoUpdate(){
        //startActivity(Intent(this@SettingActivity, UserInfoUpdateActivity::class.java))
        finish()
    }

    // 로그아웃
    private fun Logout() {
        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.clear()
        //editor.commit();
        editor.apply()
        intent = Intent(this@SettingActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        startActivity(intent)
        finish()
    }

    // 회원 탈퇴
    private fun UnRegister(){
        mUserApi.userWithDrawal(loginId).enqueue(object : Callback<ResultModel> {
            override fun onResponse(
                call: retrofit2.Call<ResultModel>,
                response: Response<ResultModel>
            ) {
                if ("success" == response.body()?.result) { // 회원 탈퇴 성공
                    startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                    finish()
                    Toast.makeText(this@SettingActivity, "$loginId 님 회원탈퇴 요청이 정상 처리되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {  // 탈퇴 실패
                    Toast.makeText(this@SettingActivity, "잠시 후 다시 확인바랍니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: retrofit2.Call<ResultModel>, t: Throwable) {
                progressDialog.dismiss()
                // 네트워크 문제
                Toast.makeText(
                    this@SettingActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    //뒤로가기 종료버튼
    override fun onBackPressed() {
        startActivity(Intent(this@SettingActivity, MainActivity::class.java))
        finish()
    }

}