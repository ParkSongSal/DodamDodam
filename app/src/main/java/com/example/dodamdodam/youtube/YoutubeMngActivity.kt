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
import kotlinx.android.synthetic.main.activity_youtube_mng.*

class YoutubeMngActivity : BaseActivity() {

    var youtubeParamV = ""

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
        Toast.makeText(applicationContext, "수정버튼", Toast.LENGTH_LONG).show()
    }
}
