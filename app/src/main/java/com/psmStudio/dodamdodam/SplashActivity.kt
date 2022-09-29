package com.psmStudio.dodamdodam

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.psmStudio.dodamdodam.utils.Common

/*
* # 로딩화면
* 1.2초 후 로그인 화면으로 이동
* */
class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        val actionBar = supportActionBar
        actionBar?.hide()

        init(this@SplashActivity)


        startLoading()
    }

    private fun startLoading() {
        //val handler = Handler()
        /*handler.postDelayed({
            Common.intentCommon(this@SplashActivity, LoginActivity::class.java)
            finish()
        }, 2000) // 2.5초후에 실행*/

        /*
        * Handler() deprecated 로 인한 코드 개선
        * */
        Handler(Looper.getMainLooper()).postDelayed({
            Common.intentCommon(this@SplashActivity, LoginActivity::class.java)
            finish()
        }, 2000)
    }
}