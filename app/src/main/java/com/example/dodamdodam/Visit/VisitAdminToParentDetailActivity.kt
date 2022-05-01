package com.example.dodamdodam.Visit

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.dodamdodam.BaseActivity
import com.example.dodamdodam.R
import com.example.dodamdodam.utils.Common
import com.example.dodamdodam.utils.ImageDetailActivity
import com.example.dodamdodam.utils.ImageSlideViewPagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*

class VisitAdminToParentDetailActivity : BaseActivity() {

    lateinit var call: Call<ResultVisit>

    lateinit var replyCall: Call<ResultData>

    var replyList: MutableList<ResultReply> = arrayListOf()

    private var mAdapter: BoardReplyDataAdapter? = null

    var mViewPagerAdapter: ImageSlideViewPagerAdapter? = null

    //var slideModels : ArrayList<SlideModel> = ArrayList<SlideModel>()
    var pathList = ArrayList<String>()
    var originalPathList = ArrayList<String>()

    private var count = 0
    var seq = -1
    var babyName : String? = null
    var parentId: String? = null
    var parentName: String? = null
    var visitNotice : String? = null
    var babyWeight : String? = null
    var babyLactation : String? = null
    var babyRequireItem : String? = null
    var babyEtc : String? = null

    private val babyNameTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyNameTxt)
    }
    private val writeNameTxt: TextView by lazy {
        findViewById<TextView>(R.id.writeNameTxt)
    }
    private val writeDateTxt: TextView by lazy {
        findViewById<TextView>(R.id.writeDateTxt)
    }
    private val babyWeightTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyWeightTxt)
    }
    private val babyLactationTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyLactationTxt)
    }
    private val babyRequireItemTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyRequireItemTxt)
    }
    private val babyEtcTxt: TextView by lazy {
        findViewById<TextView>(R.id.babyEtcTxt)
    }

    private val viewPagerMain: ViewPager by lazy {
        findViewById<ViewPager>(R.id.viewPagerMain)
    }
    private val toolbar2: Toolbar by lazy {
        findViewById<Toolbar>(R.id.toolbar2)
    }
    private val appBar: AppBarLayout by lazy {
        findViewById<AppBarLayout>(R.id.app_bar)
    }
    private val toolbarLayout: CollapsingToolbarLayout by lazy {
        findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)
    }
    private val replyTxt: EditText by lazy {
        findViewById<EditText>(R.id.ReplyTxt)
    }
    private val replyScrollView: ScrollView by lazy {
        findViewById<ScrollView>(R.id.replyScrollView)
    }
    private val noDataLl: LinearLayout by lazy {
        findViewById<LinearLayout>(R.id.noDataLl)
    }

    private val reply_list: RecyclerView by lazy {
        findViewById<RecyclerView>(R.id.reply_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_to_parent_detail)

        init(this@VisitAdminToParentDetailActivity)

        loginId = setting.getString("loginId", "").toString()

        try {
            val intent : Intent = intent
            if(intent.hasExtra("resultVisit")){
                val resultVisit = intent.getSerializableExtra("resultVisit") as ResultVisit
                Log.d("TAG", "VisitAdminToParentDetailActivity : resultVisit : $resultVisit")
                seq = resultVisit.seq
                babyName = resultVisit.babyName
                parentName = resultVisit.parentName
                parentId = resultVisit.parentId
                visitNotice = resultVisit.visitNotice
                babyWeight = resultVisit.babyWeight
                babyLactation = resultVisit.babyLactation
                babyRequireItem = resultVisit.babyRequireItem
                babyEtc = resultVisit.babyEtc

                babyNameTxt.text = resultVisit.babyName + "아기 면회소식"
                writeNameTxt.text = "관리자"
                writeDateTxt.text = Common.dataSplitFormat(resultVisit.writeDate, "date")
                babyWeightTxt.text = babyWeight
                babyLactationTxt.text = babyLactation
                babyRequireItemTxt.text = babyRequireItem
                babyEtcTxt.text = babyEtc

                pathList = intent.getSerializableExtra("pathList") as ArrayList<String>
                originalPathList = intent.getSerializableExtra("originalPathList") as ArrayList<String>

                for (j in pathList.indices){
                    if("null" == pathList[j] || "" == pathList[j]){
                        pathList[j] = "android.resource://$packageName/drawable/app_icon"
                    }else{
                        continue
                    }
                }

                for (i in pathList.indices) {
                    if(pathList[i].isNotEmpty()){
                        if(pathList[i].contains("app_icon")) {
                            continue
                        }else{
                            count++
                            //slideModels.add(SlideModel(pathList[i]))
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
        setSupportActionBar(toolbar2)
        supportActionBar?.title = ""

        appBar.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                when {
                    verticalOffset == 0 -> { //  이미지가 보일때
                        supportActionBar?.title = ""
                        toolbarLayout.title = ""
                    }
                    Math.abs(verticalOffset) >= appBar.totalScrollRange -> { // 이미지 안보이고 툴바만 보일떄
                        supportActionBar?.title = "면회소식 상세"
                    }
                    Math.abs(verticalOffset) <= appBar.totalScrollRange -> {// 중간
                        toolbarLayout.title = ""
                        supportActionBar?.subtitle = ""
                    }
                    else -> {
                        supportActionBar?.title = ""
                        toolbarLayout.title = ""
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

        //onImageClickAction(pathList, event.position)
        onImageDetailAction(pathList)
    }

    private fun onImageDetailAction(mPathList : ArrayList<String>){

        val intent = Intent(applicationContext, ImageDetailActivity::class.java)
        intent.putExtra("pathList", mPathList as Serializable)
        intent.putExtra("prevActivity","VisitAdminToParentDetailActivity")
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.btn_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            // 면회소식 Update
            R.id.updateBtn ->{
                intent = Intent(this@VisitAdminToParentDetailActivity, VisitAdminUpdateActivity::class.java)

                intent.putExtra("seq", seq)


                intent.putExtra("parentId", parentId)
                intent.putExtra("parentName",parentName)
                intent.putExtra("babyName", babyName)
                intent.putExtra("visitNotice", visitNotice)
                intent.putExtra("babyWeight", babyWeight)
                intent.putExtra("babyLactation", babyLactation)
                intent.putExtra("babyRequireItem", babyRequireItem)
                intent.putExtra("babyEtc", babyEtc)
                intent.putExtra("visitNotice", visitNotice)

                val list = ArrayList<String>()
                var originalList = ArrayList<String>()

                for (i in pathList.indices) {
                    if(pathList[i].isNotEmpty()){
                        if(pathList[i].contains("deleteiconblack2")) {
                            continue
                        }else{
                            list.add(pathList[i])
                        }
                    }else{
                        continue
                    }
                }
                for(i in originalPathList.indices){
                    if(originalPathList[i].isNotEmpty()){
                        originalList.add(originalPathList[i])
                    }
                }

                intent.putExtra("originalPathList",originalList)
                intent.putExtra("pathList",list)
                startActivity(intent)
                finish()
                true
            }

            // 면회소식 Delete
            R.id.delBtn -> {
                dlg.setTitle("삭제 알림")
                    .setMessage("게시물을 삭제 하시겠습니까?")
                    .setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                        deleteToParentBoard()
                    })
                    .setNegativeButton("아니오", null)
                dlg.show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
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
                    replyScrollView.visibility = View.GONE
                    noDataLl.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<List<ResultReply>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminToParentDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("TAG", "replyList Response " + t.message)

            }
        })
    }

    /* 면회소식 삭제 */
    fun deleteToParentBoard(){
        val boardSeqPart = RequestBody.create(MultipartBody.FORM, seq.toString())
        call = mVisitApi.admin_to_parent_board_delete(boardSeqPart)

        call.enqueue(object : Callback<ResultVisit> {

            override fun onResponse(call: Call<ResultVisit>, response: Response<ResultVisit>) {

                // 정상결과
                if (response.body()!!.result == "success") {
                    intent = Intent(
                        this@VisitAdminToParentDetailActivity,
                        VisitAdminUserSelActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    startActivity(intent)
                    finish()
                    Toast.makeText(
                        this@VisitAdminToParentDetailActivity,
                        "삭제되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    //중복인 닉네임 존재
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }
            }

            override fun onFailure(call: Call<ResultVisit>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminToParentDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun onClick(view: android.view.View) {
        when (view.id) {

            /* 댓글등록 버튼 */
            R.id.replyBtn -> {
                replyInsert()
            }

        }
    }

    /* 댓글달기 */
    fun replyInsert(){

        val boardSeq = seq.toString()
        val userId = loginId
        val replyContent = replyTxt.text.toString()
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
                        this@VisitAdminToParentDetailActivity,
                        "등록되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    replyScrollView.visibility = View.VISIBLE
                    noDataLl.visibility = View.GONE
                    replyList.clear()
                    replyTxt.text.clear()
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
                    this@VisitAdminToParentDetailActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }
    override fun onBackPressed() {
        val intent = Intent(this@VisitAdminToParentDetailActivity, VisitAdmintoParentListActivity::class.java)
        intent.putExtra("parentId", parentId)
        intent.putExtra("parentName", parentName)
        startActivity(intent)
        finish()
    } //뒤로가기 종료버튼
}