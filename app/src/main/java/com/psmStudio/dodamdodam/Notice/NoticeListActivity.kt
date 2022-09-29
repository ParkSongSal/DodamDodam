package com.psmStudio.dodamdodam.Notice

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.psmStudio.dodamdodam.BaseActivity
import com.psmStudio.dodamdodam.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeListActivity : BaseActivity() {

    var  boardList: MutableList<ResultNotice> = arrayListOf()

    private var mAdapter: NoticeDataAdapter? = null

    private val fab: FloatingActionButton by lazy {
        findViewById<FloatingActionButton>(R.id.fab)
    }

    private val EditTextFilter: EditText by lazy {
        findViewById<EditText>(R.id.EditTextFilter)
    }
    private val noDataLl: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.noDataLl)
    }
    private val recycle_view: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.recycle_view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "공지사항"
        setSupportActionBar(toolbar)

        init(this@NoticeListActivity)

        loginId = setting.getString("loginId", "").toString()

        if (loginId == "admin") {
            fab.visibility = View.VISIBLE
            // 관리자만 수정 가능
            fab.setOnClickListener {
                intent = Intent(this@NoticeListActivity, NoticeInsertActivity::class.java)
                startActivity(intent)
                finish()
            }
        } else {
            fab.visibility = View.GONE
        }

        getServerData()

        EditTextFilter.addTextChangedListener(object : TextWatcher {
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

    //게시판 리스트 조회
    private fun getServerData() {
        val call: Call<List<ResultNotice>> = mBoardApi.getNoticeList()
        call.enqueue(object : Callback<List<ResultNotice>> {
            override fun onResponse(
                call: Call<List<ResultNotice>>,
                response: Response<List<ResultNotice>>
            ) {

                Log.d("TAG", "response ${response.body()}")
                //정상 결과
                val result: List<ResultNotice> = response.body()!!
                if(result.isNotEmpty()){
                    for (i in result.indices) {
                        val SEQ: Int = result[i].seq
                        val TITLE: String = result[i].title
                        val CONTENT: String = result[i].content
                        val INSERT_ID: String = result[i].insertId
                        val INSERT_DATE: String = result[i].insertDate
                        val UPDATE_ID: String = result[i].updateId
                        val UPDATE_DATE: String = result[i].updateDate
                        val getServerdata = ResultNotice(
                            SEQ,
                            TITLE,
                            CONTENT,
                            INSERT_ID,
                            INSERT_DATE,
                            UPDATE_ID,
                            UPDATE_DATE
                        )
                        boardList.add(getServerdata)
                        //saveList.add(getServerdata)
                        mAdapter = NoticeDataAdapter(applicationContext, boardList)
                        recycle_view.adapter = mAdapter
                    }
                }else{
                    recycle_view.visibility = View.GONE
                    noDataLl.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<List<ResultNotice>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@NoticeListActivity,
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
    fun onItemClick(event: NoticeDataAdapter.ItemClickEvent) {

        val intent = Intent(applicationContext, NoticeDetailActivity::class.java)
        intent.putExtra("Seq", boardList[event.position].seq)
        intent.putExtra("TITLE", boardList[event.position].title)
        intent.putExtra("WRITER", boardList[event.position].updateId)
        intent.putExtra("CONTENT", boardList[event.position].content)
        intent.putExtra("DATE", boardList[event.position].updateDate)

        startActivity(intent)
        finish()
    }
}