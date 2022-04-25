package com.example.dodamdodam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dodamdodam.utils.BackPressedForFinish

class MainActivity : BaseActivity() {

    var backPressedForFinish : BackPressedForFinish? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init(this@MainActivity)

        loginId = setting.getString("loginId", "").toString()

        //getBabyInfo(loginId)

        backPressedForFinish = BackPressedForFinish(this@MainActivity)
    }

    fun mOnClick(view: android.view.View) {}
}