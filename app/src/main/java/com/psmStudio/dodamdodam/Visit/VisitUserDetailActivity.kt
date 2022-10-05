package com.psmStudio.dodamdodam.Visit

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.denzcoskun.imageslider.models.SlideModel
import com.psmStudio.dodamdodam.BaseActivity
import com.psmStudio.dodamdodam.R
import com.psmStudio.dodamdodam.utils.Common
import com.psmStudio.dodamdodam.utils.Common.dataSplitFormat
import com.psmStudio.dodamdodam.utils.ImageDetailActivity
import com.psmStudio.dodamdodam.utils.ImageSlideViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_visit_user_detail.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.lang.Exception
import java.util.ArrayList

class VisitUserDetailActivity : BaseActivity() {

    var writeDateAry: ArrayList<String>? = ArrayList<String>()
    lateinit var call: Call<ResultVisit>
    lateinit var replyCall: Call<ResultData>
    var mViewPagerAdapter: ImageSlideViewPagerAdapter? = null

    var slideModels : ArrayList<SlideModel> = ArrayList<SlideModel>()
    var pathList = ArrayList<String>()
    private var count = 0
    var babyName : String? = null
    var seq = -1

    var replyList: MutableList<ResultReply> = arrayListOf()
    private var mAdapter: BoardReplyDataAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_user_detail)

        init(this@VisitUserDetailActivity)

        loginId = setting.getString("loginId", "").toString()

        try {
            val intent : Intent = intent
            if(intent.hasExtra("resultVisit")){
                val resultVisit = intent.getSerializableExtra("resultVisit") as ResultVisit
                Log.d("TAG", "Detail Activity writeDateArray : ${resultVisit.toString()}")
                if("N" == resultVisit.boardConfirm){
                    boardConfirmAct(resultVisit.parentId, resultVisit.seq)
                }
                seq = resultVisit.seq
                babyName = resultVisit.babyName

                babyNameTxt.text = resultVisit.babyName + "아기 면회소식"
                writeNameTxt.text = "관리자"
                writeDateTxt.text = dataSplitFormat(resultVisit.writeDate,"date")
                babyWeightTxt.text = resultVisit.babyWeight
                babyLactationTxt.text = resultVisit.babyLactation
                babyRequireItemTxt.text = resultVisit.babyRequireItem
                babyEtcTxt.text = resultVisit.babyEtc
                Log.d("TAG", "Detail Activity Exceptilon ")
                pathList = intent.getSerializableExtra("pathList") as ArrayList<String>

                for (j in pathList.indices){
                    if("null" == pathList[j] || "" == pathList[j]){
                        pathList[j] = "android.resource://$packageName/drawable/ic_baseline_close_24"
                    }else{
                        continue
                    }
                }

                for (i in pathList.indices) {
                    if(pathList[i].isNotEmpty()){
                        if(pathList[i].contains("ic_baseline_close_24")) {
                            continue
                        }else{
                            count++
                            slideModels.add(SlideModel(pathList[i]))
                            //slideModels.add(SlideModel(pathList[i],resultVisit.babyName))
                            //setImage(pathList[i], i)
                        }
                    }else{
                        continue
                    }
                }
                getReplyList(seq)

            }

        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG", "Detail Activity Exceptilon ${e.toString()}")
        }

        mViewPagerAdapter = ImageSlideViewPagerAdapter(applicationContext, pathList)

        viewPagerMain.adapter = mViewPagerAdapter
        //img_slider.setImageList(slideModels, true)
        setSupportActionBar(toolbar3)
        supportActionBar?.title = ""

        app_bar2.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                when {
                    verticalOffset == 0 -> { //  이미지가 보일때
                        supportActionBar?.title = ""
                        toolbar_layout2.title = ""
                    }
                    Math.abs(verticalOffset) >= app_bar2.totalScrollRange -> { // 이미지 안보이고 툴바만 보일떄
                        supportActionBar?.title = "$babyName 면회소식"

                    }
                    Math.abs(verticalOffset) <= app_bar2.totalScrollRange -> {// 중간
                        toolbar_layout2.title = ""
                        supportActionBar?.subtitle=""
                    }
                    else -> {
                        supportActionBar?.title = ""
                        toolbar_layout2.title = ""
                    }
                }
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


    // 보낸이 : MemoRecyclerAdapter
    @SuppressLint("RestrictedApi")
    @Subscribe
    fun onItemClick(event: ImageSlideViewPagerAdapter.ItemClickEvent) {

        onImageDetailAction(pathList)
        //onImageClickAction(pathList, event.position)
    }
    private fun onImageDetailAction(mPathList : ArrayList<String>){

        val intent = Intent(applicationContext, ImageDetailActivity::class.java)
        intent.putExtra("pathList", mPathList as Serializable)
        intent.putExtra("prevActivity","VisitUserDetailActivity")

        startActivity(intent)
    }

    fun getReplyList(seq : Int){
        val boardSeq = seq.toString()
        val replyBoardSeqPart = RequestBody.create(MultipartBody.FORM, boardSeq)

        val call2 : Call<List<ResultReply>> = mVisitApi.replyList(replyBoardSeqPart)
        call2.enqueue(object : Callback<List<ResultReply>> {
            override fun onResponse(
                call: Call<List<ResultReply>>,
                response: Response<List<ResultReply>>
            ) {

                Log.d("TAG", "replyList Response ${response.body()}")
                //정상 결과
                val result: List<ResultReply> = response.body()!!
                if(result.isNotEmpty()){
                    for (i in result.indices) {
                        val seq: String = result[i].seq
                        val userId: String = result[i].userId
                        val replyContent: String = result[i].replyContent
                        val insertDate: String = result[i].insertDate
                        val parentName : String = result[i].parentName
                        val getServerdata = ResultReply(
                            seq,
                            userId,
                            replyContent,
                            insertDate,
                            parentName
                        )
                        replyList.add(getServerdata)
                        mAdapter = BoardReplyDataAdapter(applicationContext, replyList)
                        reply_list.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()

                    }
                }else{
                    scroll_view.visibility = View.GONE
                    noDataLl.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<List<ResultReply>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitUserDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    /* 댓글달기 */
    fun replyInsert(){

        val boardSeq = seq.toString()
        val userId = loginId
        val replyContent = ReplyTxt.text.toString()
        val insertDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
        if (replyContent == "" || replyContent.isEmpty()) {

            dlg.setMessage("빈칸은 등록할 수 없습니다.")
                .setNegativeButton("확인", null)
                .create()
            dlg.show()

            return
        }


        val replyBoardSeqPart = RequestBody.create(MultipartBody.FORM, boardSeq)
        val replyUserIdPart = RequestBody.create(MultipartBody.FORM, userId)
        val replyContentPart = RequestBody.create(MultipartBody.FORM, replyContent)
        val replyInsertDatePart = RequestBody.create(MultipartBody.FORM, insertDate)

        replyCall = mVisitApi.replyInsert(
            replyBoardSeqPart,
            replyUserIdPart,
            replyContentPart,
            replyInsertDatePart
        )
        replyCall.enqueue(object : Callback<ResultData> {

            override fun onResponse(call: Call<ResultData>, response: Response<ResultData>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    Toast.makeText(
                        this@VisitUserDetailActivity,
                        "등록되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    scroll_view.visibility = View.VISIBLE
                    noDataLl.visibility = View.GONE
                    replyList.clear()
                    ReplyTxt.text.clear()
                    getReplyList(seq)
                } else {
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultData>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitUserDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun boardConfirmAct(userId : String?, seq : Int){
        if(userId != null){
            val parentId = userId
            val boardConfirmYn = "Y"
            val boardConfirmDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")

            val seqPart = RequestBody.create(MultipartBody.FORM, seq.toString())
            val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
            val boardConfirmYnPart = RequestBody.create(MultipartBody.FORM, boardConfirmYn)
            val boardConfirmDatePart = RequestBody.create(MultipartBody.FORM, boardConfirmDate)

            call = mVisitApi.to_parent_board_confirm_update(
                seqPart,
                parentIdPart,
                boardConfirmYnPart,
                boardConfirmDatePart
            )

            call.enqueue(object : Callback<ResultVisit> {
                override fun onResponse(call: Call<ResultVisit>, response: Response<ResultVisit>) {
                    // 정상결과
                    if (response.body()!!.result == "success") {

                        Toast.makeText(
                            this@VisitUserDetailActivity,
                            "면회 소식을 확인하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        dlg.setMessage("다시 시도 바랍니다.")
                            .setNegativeButton("확인", null)
                        dlg.show()
                    }
                }

                override fun onFailure(call: Call<ResultVisit>, t: Throwable) {
                    // 네트워크 문제
                    Toast.makeText(
                        this@VisitUserDetailActivity,
                        "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }else{
            Log.d("TAG", "userId Null...")
        }
    }

    fun onClick(view: View) {}

    //뒤로가기 종료버튼
    override fun onBackPressed() {
        val intent = Intent(this@VisitUserDetailActivity, VisitParentCalendarActivity::class.java)
        startActivity(intent)
        finish()
    }

}