package com.example.dodamdodam.Introduce

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.dodamdodam.BaseActivity
import com.example.dodamdodam.R

class EnterIntroduceActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_introduce)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "입원 안내문"
        setSupportActionBar(toolbar)

        init(applicationContext)
        introduceValidate("1")  // 입원 안내문

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if(loginId == "admin"){
            modifyBtn.visibility = View.VISIBLE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener{
                var intent = Intent(this@EnterIntroduceActivity, AppIntroduceModifyActivity::class.java)
                intent.putExtra("boardGubun", "1")
                intent.putExtra("actGubun", actGubun)
                if(actGubun == "update"){
                    intent.putExtra("content",enterIntroTxt.text)
                }
                startActivity(intent)
                finish()
            }
        }else{
            modifyBtn.visibility = View.GONE
        }

        introduceList("1")
    }
}