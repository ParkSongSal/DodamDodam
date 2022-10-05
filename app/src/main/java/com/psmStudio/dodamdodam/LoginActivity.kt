package com.psmStudio.dodamdodam

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.psmStudio.dodamdodam.Retrofit2.ResultModel
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    val READ_PHONE_PERMISSON = 1

    var userId = ""
    var userPassword = ""

    private val idEditText: EditText by lazy {
        findViewById<EditText>(R.id.idEditText)
    }

    private val pwEditText: EditText by lazy {
        findViewById<EditText>(R.id.pwEditText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        init(this@LoginActivity)

        //권한 체크
        TedPermission.with(applicationContext)
            .setPermissionListener(permissionListener)
            .setDeniedMessage("접근 거부하셨습니다.\n[설정] - [권한]에서 권한을 허용해주세요.")
            .setPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CAMERA
            )
            .check()
    }

    fun onClick(view: android.view.View) {

        when (view.id) {
            // 로그인
            R.id.loginBtn -> {
                userId = idEditText.text.toString()
                userPassword = pwEditText.text.toString()

                progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog.show()

                handler.postDelayed({
                    loginAct(userId, userPassword)
                }, 1000) // 2.5초후에 실행

            }
            // 회원가입
            R.id.registerBtn -> register()
        }
    }

    // 로그인 처리
    private fun loginAct(userId: String, userPassword: String) {

        if ("" == userId) {
            Toast.makeText(this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show()
            return;
        } else if ("" == userPassword) {
            Toast.makeText(this, "패스워드를 입력해주세요", Toast.LENGTH_SHORT).show()
            return;
        }
        mUserApi.userLogin(userId, userPassword).enqueue(object : Callback<ResultModel> {
            override fun onResponse(
                call: retrofit2.Call<ResultModel>,
                response: Response<ResultModel>
            ) {

                if ("success" == response.body()?.result) { // 로그인 성공

                    editor.putString("loginId", userId)
                    editor.apply()

                    //로그인 성공
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()

                    Toast.makeText(this@LoginActivity, "$userId 님 환영합니다!", Toast.LENGTH_SHORT)
                        .show()
                } else {  // 로그인 실패
                    Toast.makeText(this@LoginActivity, "입력하신 정보를 다시 확인바랍니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: retrofit2.Call<ResultModel>, t: Throwable) {

                progressDialog.dismiss()
                // 네트워크 문제
                Toast.makeText(
                    this@LoginActivity,
                    "데이터 접속 w상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

    }

    // 회원가입 화면 이동
    private fun register() {
        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
    }

    private var permissionListener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(applicationContext, "권한이 허용됨", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(applicationContext, "권한이 거부됨", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_PHONE_PERMISSON -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "권한 승인함", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "권한 거부함", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}