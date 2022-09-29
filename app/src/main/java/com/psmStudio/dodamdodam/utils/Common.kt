package com.psmStudio.dodamdodam.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Handler
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Common {
    fun dataSplitFormat(data: String?, type: String): String {
        val resultData: Array<String>
        var result = ""
        if (data != null) {
            if (data != "") {
                if ("date" == type) {
                    resultData = data.split("-".toRegex()).toTypedArray()
                    result = resultData[0] + "년" + resultData[1] + "월" + resultData[2] + "일"
                } else if ("time" == type) {
                    resultData = data.split(":".toRegex()).toTypedArray()
                    result = " " + resultData[0] + "시" + resultData[1] + "분 출생"
                }
            } else {
                if ("date" == type) {
                    result = "출생일자 미등록"
                }
            }
        } else {
            result = "미등록"
        }
        return result
    }

    //현재 시간
    fun nowDate(format: String?): String {
        // 현재시간을 msec 으로 구한다.
        val now = System.currentTimeMillis()
        // 현재시간을 date 변수에 저장한다.
        val nowdate = Date(now)
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        val sdfNow = SimpleDateFormat(format)
        // nowDate 변수에 값을 저장한다.
        return sdfNow.format(nowdate)
    }

    // DateFormatting
    fun formatDate(date: String?, format: String): String {

        val tempDate = SimpleDateFormat(format)
        var temDate: Date? = null
        temDate = tempDate.parse(date)

        return tempDate.format(temDate)
    }

    fun formatTimeString(regTime: String): String {
        val tempDate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        var temdate: Date? = null
        var diffTime: Long = 0
        var time: Long = 0
        var curTime: Long = 0
        var msg: String? = null
        try {
            temdate = tempDate.parse(regTime)
            time = temdate.time
            curTime = System.currentTimeMillis()
            diffTime = (curTime - time) / 1000
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전"
        } else if (TIME_MAXIMUM.SEC.let { diffTime /= it; diffTime } < TIME_MAXIMUM.MIN) {
            msg = diffTime.toString() + "분 전"
        } else if (TIME_MAXIMUM.MIN.let { diffTime /= it; diffTime } < TIME_MAXIMUM.HOUR) {
            msg = diffTime.toString() + "시간 전"
        } /*else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        }*/ else {
            msg = regTime.substring(0, 16) + ""
            Log.d("board", "날짜 ===> $msg")
        }
        return msg
    }

    fun Spannable(str: String?, start: Int, end: Int): SpannableStringBuilder {
        val color = Color.rgb(240, 130, 95)
        val sb = SpannableStringBuilder()
        sb.append(str)
        sb.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sb
    }

    fun intentCommon(activity: Activity, cls2: Class<*>?) {
        val intent = Intent(activity, cls2)
        activity.startActivity(intent)
    }

    fun fileDataProcessing(filePath: String): Uri {
        return if (filePath.contains("(")) {
            val idx = filePath.indexOf("(")
            val path = filePath.substring(0, idx)
            Uri.fromFile(File(path))
        } else {
            Uri.fromFile(File(filePath))
        }
    }

    // 몇분 전, 방금 전
    private object TIME_MAXIMUM {
        const val SEC = 60 //초
        const val MIN = 60 //분
        const val HOUR = 24 //시
        const val DAY = 30 //일
        const val MONTH = 12 //월
    }

    //ProgressDialog 공통
    class ProgressDialogHandler(context: Context?) : Handler() {
        private val START_PROGRESSDIALOG = 100
        private val END_PROGRESSDIALOG = 101
        var mProgressDialog: ProgressDialog? = null
        private var context: Context? = null
        fun handleMessage(gubun: Int) {
            when (gubun) {
                START_PROGRESSDIALOG -> {
                    if (mProgressDialog == null) {
                        mProgressDialog = ProgressDialog(context)
                        mProgressDialog!!.setTitle("Working...")
                        mProgressDialog!!.setMessage("wait for complete working..")
                    }
                    mProgressDialog!!.show()
                }
                END_PROGRESSDIALOG -> if (mProgressDialog != null) {
                    mProgressDialog!!.dismiss()
                }
            }
        }

        init {
            this.context = context
        }
    }
}