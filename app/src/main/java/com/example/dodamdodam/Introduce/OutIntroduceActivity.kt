package com.example.dodamdodam.Introduce

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.dodamdodam.BaseActivity
import com.example.dodamdodam.MainActivity
import com.example.dodamdodam.R
import com.example.dodamdodam.Retrofit2.ResultIntroduceImg
import com.example.dodamdodam.utils.Common
import com.example.dodamdodam.utils.FileUtils
import kotlinx.android.synthetic.main.activity_enter_introduce.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OutIntroduceActivity : BaseActivity() {

    var PICK_IMAGE = 1

    var imageUri: Uri? = null

    lateinit var updateCall: Call<ResponseBody>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_out_introduce)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "퇴원 안내문"
        setSupportActionBar(toolbar)

        init(applicationContext)
        getIntroduceImg("2")

        setting = getSharedPreferences("setting", MODE_PRIVATE)
        editor = setting.edit()
        editor.apply()


        loginId = setting.getString("loginId", "").toString()

        if(loginId == "admin"){
            saveCardView.visibility = View.VISIBLE
            imageSelectCardView.visibility = View.VISIBLE
            updateDateCardView.visibility = View.GONE
            // 관리자만 수정 가능
            modifyBtn.setOnClickListener {
                Handler().postDelayed(Runnable {
                    updateEnterIntroduce()
                    //여기에 딜레이 후 시작할 작업들을 입력
                }, 2000) // 0.5초 정도 딜레이를 준 후 시작

            }
        }else{
            updateDateCardView.visibility = View.VISIBLE
            saveCardView.visibility = View.GONE
            imageSelectCardView.visibility = View.GONE
        }

        // 입원 이미지 선택
        imageSelectBtn.setOnClickListener {
            selectImage()
        }

    }


    private fun getIntroduceImg(boardGubun : String){
        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)

        mBoardApi.getIntroduceImg(boardGubunPart).enqueue(object : Callback<List<ResultIntroduceImg>> {
            override fun onResponse(call: Call<List<ResultIntroduceImg>>, response: Response<List<ResultIntroduceImg>>) {

                //정상 결과
                val result: List<ResultIntroduceImg>? = response.body()
                for (i in result!!.indices) {

                    updateDateTxt.text = "관리자  " + result[i].update_date


                    Glide.with(applicationContext)
                        .load(result[i].path)
                        .fitCenter()
                        .into(enterImg)
                }

            }

            override fun onFailure(call: Call<List<ResultIntroduceImg>>, t: Throwable) {
                validate = false
                // 네트워크 문제
                Toast.makeText(
                    coxt,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun updateEnterIntroduce(){

        val boardGubun = "2"    // 1 : 입원, 2: 퇴원
        val updateId = loginId
        val updateDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val boardGubunPart = RequestBody.create(MultipartBody.FORM, boardGubun)
        val updateIdPart = RequestBody.create(MultipartBody.FORM, updateId)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, updateDate)

        val getImage = FileUtils.getFile(this@OutIntroduceActivity, imageUri)

        val originalPath = RequestBody.create(MultipartBody.FORM, imageUri.toString())
        val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), getImage)
        val file1 = MultipartBody.Part.createFormData("image[]", getImage?.name, imagePart)
        updateCall = mBoardApi.introduceImgUpdate(
            boardGubunPart,
            file1,
            originalPath,
            updateIdPart,
            updateDatePart
        )

        updateCall.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if(response.isSuccessful){
                    intent = Intent(this@OutIntroduceActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@OutIntroduceActivity, "수정되었습니다.", Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@OutIntroduceActivity, "다시 시도 바랍니다.", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(
                call: Call<ResponseBody?>,
                t: Throwable
            ) {
                Toast.makeText(
                    this@OutIntroduceActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()

            }
        })
    }
    
    private fun selectImage() {

        val intent = Intent()

        // setting type to select to be image
        intent.type = "image/*"

        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE
        )

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data?.data
            Glide.with(applicationContext)
                .load(imageUri)
                .fitCenter()
                .into(enterImg)
        }
    }
    
}