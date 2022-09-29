package com.psmStudio.dodamdodam.Visit

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.psmStudio.dodamdodam.BaseActivity
import com.psmStudio.dodamdodam.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.psmStudio.fastcampusandroid.Calendar.EventDecorator
import com.psmStudio.fastcampusandroid.Calendar.SaturdayDecorator
import com.psmStudio.fastcampusandroid.Calendar.SundayDecorator
import kotlinx.android.synthetic.main.activity_visit_parent_calendar.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class VisitParentCalendarActivity : BaseActivity() {

    var parentName : String? = ""
    var parentId : String? = ""
    var writeDateAry = ArrayList<ResultVisit>()
    var detailAry = ArrayList<ResultVisit>()

    val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_parent_calendar)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "면회소식"
        setSupportActionBar(toolbar)

        init(this@VisitParentCalendarActivity)

        loginId = setting.getString("loginId", "").toString()

        getToParentBoard(loginId)


        calendarView.state().edit()
            .setFirstDayOfWeek(Calendar.SUNDAY)
            .setMinimumDate(CalendarDay.from(2017, 0, 1))   //달력의 시작
            .setMaximumDate(CalendarDay.from(2030, 11, 31))   //달력의 끝
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        calendarView.selectedDate = CalendarDay.today()

        calendarView.addDecorators(
            SundayDecorator(),
            SaturdayDecorator()
        )


        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            val Year = date.year
            val Month = date.month + 1
            val Day = date.day
            val shot_Day = if (Month < 10) {
                if(Day < 10){
                    "$Year-0$Month-0$Day"
                }else{
                    "$Year-0$Month-$Day"
                }
            } else {
                if(Day < 10){
                    "$Year-$Month-0$Day"
                }else{
                    "$Year-$Month-$Day"
                }
            }
            calendarView.clearSelection()
            for (i in writeDateAry.indices) {
                if (shot_Day == writeDateAry[i].writeDate) {
                    parentNameTxt.text = writeDateAry[i].parentName + " 보호자님께"
                    writeDateTxt.text = "작성일자 : " + writeDateAry[i].writeDate
                    detailBtn.visibility = View.VISIBLE
                    detailAry.add(writeDateAry[i])
                }
            }

        })

        detailBtn.setOnClickListener{

            intent = Intent(this@VisitParentCalendarActivity, VisitUserDetailActivity::class.java)

            val resultVisit : ResultVisit = detailAry[0]

            val list = java.util.ArrayList<String>()

            list.add(detailAry[0].path1.toString())
            list.add(detailAry[0].path2.toString())
            list.add(detailAry[0].path3.toString())

            intent.putExtra("pathList", list as Serializable)
            intent.putExtra("resultVisit", resultVisit as Serializable)
            startActivity(intent)
            finish()
        }

    }

    fun getToParentBoard(parentId: String?){

        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)

        val call: Call<List<ResultVisit>> = mVisitApi.toParentListUser(parentIdPart)
        call.enqueue(object : Callback<List<ResultVisit>> {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<List<ResultVisit>>,
                response: Response<List<ResultVisit>>
            ) {

                Log.d("TAG", "response ${response.body()}")
                //정상 결과
                val result: List<ResultVisit> = response.body()!!
                for (i in result.indices) {
                    val seq : Int = result[i].seq
                    val userId: String? = result[i].userId
                    val parentName: String? = result[i].parentName
                    val visitNotice: String? = result[i].visitNotice
                    val babyWeight = result[i].babyWeight
                    val babyLactation = result[i].babyLactation
                    val babyRequireItem = result[i].babyRequireItem
                    val babyEtc = result[i].babyEtc
                    val writeDate: String = result[i].writeDate
                    val boardConfirm = result[i].boardConfirm
                    val tempYn = result[i].tempYn
                    val reserveDate = result[i].reserveDate
                    val path1 = result[i].path1
                    val path2 = result[i].path2
                    val path3 = result[i].path3
                    //val replyCnt = result[i].replyCnt
                    val insertDate = result[i].insertDate
                    val babyName = result[i].babyName

                    val getServerdata = ResultVisit(
                        seq,
                        userId,
                        parentName,
                        visitNotice,
                        babyWeight,
                        babyLactation,
                        babyRequireItem,
                        babyEtc,
                        writeDate,
                        boardConfirm,
                        tempYn,
                        reserveDate,
                        path1,
                        path2,
                        path3,
                        null,
                        null,
                        null,
                        //null,
                        insertDate,
                        babyName
                    )

                    writeDateAry.add(getServerdata)
                    //saveList.add(getServerdata)

                    for (i in writeDateAry.indices) {
                        val time = writeDateAry[i].writeDate.split("-".toRegex()).toTypedArray()
                        //val time: Array<String> = result[i].split(",".toRegex()).toTypedArray()

                        val year = time[0].toInt()
                        val month = time[1].toInt()
                        val dayy = time[2].toInt()

                        calendar.set(Calendar.YEAR, year)

                        when (month) {
                            1 -> calendar.set(Calendar.MONTH, 0)
                            2 -> calendar.set(Calendar.MONTH, 1)
                            3 -> calendar.set(Calendar.MONTH, 2)
                            4 -> calendar.set(Calendar.MONTH, 3)
                            5 -> calendar.set(Calendar.MONTH, 4)
                            6 -> calendar.set(Calendar.MONTH, 5)
                            7 -> calendar.set(Calendar.MONTH, 6)
                            8 -> calendar.set(Calendar.MONTH, 7)
                            9 -> calendar.set(Calendar.MONTH, 8)
                            10 -> calendar.set(Calendar.MONTH, 9)
                            11 -> calendar.set(Calendar.MONTH, 10)
                            12 -> calendar.set(Calendar.MONTH, 11)
                            else -> Log.d("TAG", "no Point!")
                        }
                        calendar.set(Calendar.DATE, dayy)

                        calendarView.addDecorator(
                            EventDecorator(
                                getColor(R.color.colorAccent), Collections.singleton(
                                    CalendarDay.from(
                                        calendar
                                    )
                                )
                            )
                        )
                    }
                }
            }

            override fun onFailure(call: Call<List<ResultVisit>>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitParentCalendarActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

}