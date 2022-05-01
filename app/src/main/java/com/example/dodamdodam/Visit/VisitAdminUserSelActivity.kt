package com.example.dodamdodam.Visit

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dodamdodam.BaseActivity
import com.example.dodamdodam.R
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitAdminUserSelActivity : BaseActivity() {

    var userList: MutableList<ResultVisit> = arrayListOf()

    private var mAdapter: VisitUserDataAdapter? = null

    private val userSelectEdit: EditText by lazy {
        findViewById<EditText>(R.id.userSelectEdit)
    }

    private val recycleView: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycle_view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_user_sel)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "보호자 선택"
        setSupportActionBar(toolbar)

        init(this@VisitAdminUserSelActivity)

        loginId = setting.getString("loginId", "").toString()

        getUserList()

        // 보호자 검색
        userSelectEdit.addTextChangedListener (object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Do Nothing
            }
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mAdapter?.filter?.filter(charSequence)
            }
            override fun afterTextChanged(charSequence: Editable?) {
                //Do Nothing
            }
        })
    }

    // 보호자 리스트 조회
    private fun getUserList() {
        val call: Call<List<ResultVisit>> = mVisitApi.VisitAdminUserList()
        call.enqueue(object : Callback<List<ResultVisit>> {
            override fun onResponse(
                call: Call<List<ResultVisit>>,
                response: Response<List<ResultVisit>>
            ) {
                //정상 결과
                val result: List<ResultVisit> = response.body()!!
                for (i in result.indices) {
                    val USER_ID: String? = result[i].userId
                    val USER_NAME: String? = result[i].userName
                    val USER_PHONE: String? = result[i].userPhone
                    val BABY_NAME: String? = result[i].babyName
                    val BABY_NUM: String? = result[i].babyNum
                    val BABY_RELATION: String? = result[i].babyRelation
                    val getServerdata = ResultVisit(
                        USER_ID,
                        USER_NAME,
                        USER_PHONE,
                        BABY_NAME,
                        BABY_NUM,
                        BABY_RELATION
                    )
                    userList.add(getServerdata)
                    //saveList.add(getServerdata)
                    mAdapter = VisitUserDataAdapter(applicationContext, userList)
                    recycleView.adapter = mAdapter
                }
            }

            override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminUserSelActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    // 보낸이 : NoticeDataAdapter
    @SuppressLint("RestrictedApi")
    @Subscribe
    fun onItemClick(event: VisitUserDataAdapter.ItemClickEvent) {

        val parentName = userList[event.position].userName
        val parentId = userList[event.position].userId
        dlg.setTitle("보호자 선택 알림")
            .setMessage("선택하신 보호자($parentName)에게 작성한 게시글을 확인하시겠습니까?")
            .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                var intent = Intent(this@VisitAdminUserSelActivity, VisitAdmintoParentListActivity::class.java)
                intent.putExtra("parentId", parentId)
                intent.putExtra("parentName", parentName)
                startActivity(intent)
                finish()
            })
            .setNegativeButton("아니오", null)
        dlg.show()

    }
}