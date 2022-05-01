package com.example.dodamdodam.utils

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.dodamdodam.MainActivity
import com.example.dodamdodam.R
import java.util.ArrayList

class ImageDetailActivity : AppCompatActivity() {

    private var sliderViewPager: ViewPager2? = null
    private var layoutIndicator: LinearLayout? = null
    var pathList = ArrayList<String>()
    var images : Array<String?> = arrayOfNulls(3)

    var prevActivity : String? = ""

    private val toolbar: Toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "이미지 상세"
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

         sliderViewPager = findViewById(R.id.sliderViewPager)
        layoutIndicator = findViewById(R.id.layoutIndicators)

        val intent : Intent = intent

        pathList = intent.getSerializableExtra("pathList") as ArrayList<String>
        prevActivity = intent.getStringExtra("prevActivity")



        Log.d("TAG", "ImageDetail : $pathList")
        images[0] = pathList[0]
        images[1] = pathList[1]
        images[2] = pathList[2]
        sliderViewPager!!.offscreenPageLimit = 1
        sliderViewPager!!.adapter = ImageSlider_Adapter(this, images)

        sliderViewPager!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        setupIndicators(images.size)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                //toolbar의 back키 눌렀을 때 동작
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupIndicators(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(16, 8, 16, 8)
        for (i in indicators.indices) {
            indicators[i] = ImageView(this)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.bg_indicator_inactive
                )
            )
            indicators[i]!!.layoutParams = params
            layoutIndicator!!.addView(indicators[i])
        }
        setCurrentIndicator(0)
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = layoutIndicator!!.childCount
        for (i in 0 until childCount) {
            val imageView = layoutIndicator!!.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                    )
                )
            }
        }
    }

    //뒤로가기 종료버튼
    override fun onBackPressed() {
        // BackPressedForFinish 클래시의 onBackPressed() 함수를 호출한다.
        if(prevActivity != ""){
            finish()
        }else{
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}