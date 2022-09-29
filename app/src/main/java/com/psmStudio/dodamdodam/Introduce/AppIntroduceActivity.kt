package com.psmStudio.dodamdodam.Introduce

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.psmStudio.dodamdodam.BaseActivity
import com.psmStudio.dodamdodam.R

class AppIntroduceActivity : BaseActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_introduce)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "앱 소개"
        setSupportActionBar(toolbar)
        init(applicationContext)
        introduceValidate("0")  // 앱 소개문

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if (loginId != "admin") {
            modifyBtn.visibility = View.VISIBLE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener {


                if (validate) {
                    val intent =
                        Intent(this@AppIntroduceActivity, AppIntroduceModifyActivity::class.java)


                    intent.putExtra("boardGubun", "0")
                    intent.putExtra("actGubun", actGubun)
                    if(actGubun == "update"){
                        intent.putExtra("content",appIntroTxt.text)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        this@AppIntroduceActivity,
                        "다시 시도 바랍니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }
        } else {
            modifyBtn.visibility = View.GONE
        }

        introduceList("0")
    }
}