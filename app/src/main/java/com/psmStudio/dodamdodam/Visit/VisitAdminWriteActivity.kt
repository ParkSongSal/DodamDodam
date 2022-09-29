package com.psmStudio.dodamdodam.Visit

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.psmStudio.dodamdodam.BaseActivity
import com.psmStudio.dodamdodam.MainActivity
import com.psmStudio.dodamdodam.R
import com.psmStudio.dodamdodam.utils.Common
import com.psmStudio.dodamdodam.utils.FileUtils
import com.github.siyamed.shapeimageview.RoundedImageView
import kotlinx.android.synthetic.main.activity_visit_admin_write.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class VisitAdminWriteActivity : BaseActivity() {

    var PICK_IMAGE_MULTIPLE = 1
    val IMAGE_LIMIT = 3

    val PICKER_REQUEST_CODE = 101


    lateinit var call: Call<ResponseBody>

    var parentName : String? = ""
    var parentId : String? = ""

    var saveGubun = 0

    private var bitmap: Bitmap? = null
    private var path: Uri? = null

    private var pathsList = emptyArray<String>()
    private var uriList : MutableList<String> = mutableListOf()
    private var uriList2 : MutableList<Uri> = mutableListOf()

    private var mUriList: MutableList<Uri> = mutableListOf()

    var count = 0
    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_visit_admin_write)

        toolbar.setTitleTextColor(getColor(R.color.white))
        toolbar.title = "면회소식 등록"
        setSupportActionBar(toolbar)

        init(this@VisitAdminWriteActivity)


        if(intent != null){
            parentName = intent.getStringExtra("parentName")
            parentId = intent.getStringExtra("parentId")
        }else{
            Common.intentCommon(this@VisitAdminWriteActivity, MainActivity::class.java)
            Toast.makeText(applicationContext, "잘못된 경로입니다.", Toast.LENGTH_SHORT).show()
            finish()
        }

        loginId = setting.getString("loginId", "").toString()

        val items = resources.getStringArray(R.array.saveWay)
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)
        saveSpinner.adapter = myAdapter

        saveSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {

                var dp =  resources.displayMetrics.density
                var layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (80 * dp).toInt()
                )
                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when (position) {
                    0 -> {
                        saveGubun = 0
                        reservLl.visibility = View.GONE
                    }
                    1 -> {
                        saveGubun = 1
                        reservLl.visibility = View.GONE
                    }
                    2 -> {
                        saveGubun = 2
                        reservLl.visibility = View.VISIBLE
                    }
                    else -> {
                        saveGubun = 0
                        reservLl.visibility = View.GONE
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        insertBtn.setOnClickListener(View.OnClickListener {
            adminWriteAct(saveGubun, mUriList)
        })

        this.InitializeListener()

        saveReserveDate.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                //  .. 포커스시
                val dialog = DatePickerDialog(this, dateCallbackMethod, 2021, 6, 10)

                dialog.show()
            } else {
                //  .. 포커스 뺏겼을 때
            }
        }

        saveReserveTime.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                //  .. 포커스시
                val dialog = TimePickerDialog(
                    this,
                    android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                    timeCallbackMethod,
                    8,
                    10,
                    false
                )

                dialog.show()
            } else {
                //  .. 포커스 뺏겼을 때
            }
        }
    }
    private fun adminWriteAct(saveGubun: Int, filePath: MutableList<Uri>?) {
        var tempYn: String = "N"
        var tempYnPart : RequestBody? = null

        val reserveDate: String
        var reserveDatePart : RequestBody? = null

        var writeDate = Common.nowDate("yyyy-MM-dd")
        when (saveGubun) {
            1 -> {     //임시저장
                tempYn = "Y"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)
            }
            2 -> {      // 예약저장
                tempYn = "N"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)

                reserveDate =
                    saveReserveDate.text.toString() + " " + saveReserveTime.text.toString()
                writeDate = saveReserveDate.text.toString()
                reserveDatePart = RequestBody.create(MultipartBody.FORM, reserveDate)
            }
            else -> {
                tempYn = "N"
                tempYnPart = RequestBody.create(MultipartBody.FORM, tempYn)
                reserveDate = Common.nowDate("yyyy-MM-dd HH:mm:ss")
                reserveDatePart = RequestBody.create(MultipartBody.FORM, reserveDate)
            }
        }


        val babyWeight = babyWeightTxt.text.toString()
        val babyLactation = babyLactationTxt.text.toString()
        val babyRequireItem = babyRequireItemTxt.text.toString()
        val babyEtc = babyEtcTxt.text.toString()

        val date = Common.nowDate("yyyy-MM-dd HH:mm:ss")

        val parentIdPart = RequestBody.create(MultipartBody.FORM, parentId)
        val parentNamePart = RequestBody.create(MultipartBody.FORM, parentName)
        val babyWeightPart = RequestBody.create(MultipartBody.FORM, babyWeight)
        val babyLactationPart = RequestBody.create(MultipartBody.FORM, babyLactation)
        val babyRequireItemPart = RequestBody.create(MultipartBody.FORM, babyRequireItem)
        val babyEtcPart = RequestBody.create(MultipartBody.FORM, babyEtc)
        val writeDatePart = RequestBody.create(MultipartBody.FORM, writeDate)
        val insertIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val insertDatePart = RequestBody.create(MultipartBody.FORM, date)
        val updateIdPart = RequestBody.create(MultipartBody.FORM, loginId)
        val updateDatePart = RequestBody.create(MultipartBody.FORM, date)

        when(filePath?.size){
            0 -> {
                call = mVisitApi.toParentInsertNoImage(
                    parentIdPart,
                    parentNamePart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            1 -> {
                val originalPath = RequestBody.create(MultipartBody.FORM, filePath[0].toString())

                val f1: File? = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1?.name, imagePart)

                call = mVisitApi.toParentInsert(
                    parentIdPart,
                    parentNamePart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    null,
                    null,
                    originalPath,
                    null,
                    null,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            2 -> {

                val originalPath = RequestBody.create(MultipartBody.FORM, filePath[0].toString())
                val originalPath2 = RequestBody.create(MultipartBody.FORM, filePath[1].toString())

                val f1: File? = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1?.name, imagePart)

                val f2: File? = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[1])
                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2?.name, imagePart2)

                call = mVisitApi.toParentInsert(
                    parentIdPart,
                    parentNamePart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    file2,
                    null,
                    originalPath,
                    originalPath2,
                    null,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
            3 -> {

                val originalPath = RequestBody.create(MultipartBody.FORM, filePath[0].toString())
                val originalPath2 = RequestBody.create(MultipartBody.FORM, filePath[1].toString())
                val originalPath3 = RequestBody.create(MultipartBody.FORM, filePath[2].toString())

                val f1: File? = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[0])
                val imagePart = RequestBody.create(MediaType.parse("multipart/form-data"), f1)
                val file1 = MultipartBody.Part.createFormData("image[]", f1?.name, imagePart)

                val f2: File? = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[1])
                val imagePart2 = RequestBody.create(MediaType.parse("multipart/form-data"), f2)
                val file2 = MultipartBody.Part.createFormData("image[]", f2?.name, imagePart2)

                val f3: File? = FileUtils.getFile(this@VisitAdminWriteActivity, filePath[2])
                val imagePart3 = RequestBody.create(MediaType.parse("multipart/form-data"), f3)
                val file3 = MultipartBody.Part.createFormData("image[]", f3?.name, imagePart3)
                call = mVisitApi.toParentInsert(
                    parentIdPart,
                    parentNamePart,
                    null,
                    babyWeightPart,
                    babyLactationPart,
                    babyRequireItemPart,
                    babyEtcPart,
                    writeDatePart,
                    tempYnPart,
                    reserveDatePart,
                    file1,
                    file2,
                    file3,
                    originalPath,
                    originalPath2,
                    originalPath3,
                    insertIdPart,
                    insertDatePart,
                    updateIdPart,
                    updateDatePart
                )
            }
        }



        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                if (response.isSuccessful) {
                    intent = Intent(
                        this@VisitAdminWriteActivity,
                        VisitAdmintoParentListActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    intent.putExtra("parentId", parentId)
                    startActivity(intent)
                    finish()
                    Toast.makeText(this@VisitAdminWriteActivity, "등록되었습니다.", Toast.LENGTH_LONG)
                        .show()
                } else {
                    dlg.setMessage("다시 시도 바랍니다.")
                        .setNegativeButton("확인", null)
                    dlg.show()
                }

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 네트워크 문제
                Toast.makeText(
                    this@VisitAdminWriteActivity,
                    "데이터 접속 상태를 확인 후 다시 시도해주세요.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    fun InitializeListener() {
        dateCallbackMethod =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val month = when (monthOfYear+1) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> monthOfYear.toString()
                }
                val day = when (dayOfMonth) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> dayOfMonth.toString()
                }

                saveReserveDate.setText(
                    "$year-$month-$day"
                )
            }

        timeCallbackMethod =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val hour = when (hourOfDay) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> hourOfDay.toString()
                }
                val min = when (minute) {
                    1 -> "01"
                    2 -> "02"
                    3 -> "03"
                    4 -> "04"
                    5 -> "05"
                    6 -> "06"
                    7 -> "07"
                    8 -> "08"
                    9 -> "09"
                    else -> minute.toString()
                }
                saveReserveTime.setText("$hour:$min")
            }
    }

    fun onClick(view: View) {
        when (view.id) {
            /* 앱 소개 */
            R.id.imgRL,
            R.id.cameraIcon,
            R.id.imageTxtCount -> {
                selectImage()
            }

        }
    }


    private fun selectImage() {
        val intent = Intent()

        // setting type to select to be image
        intent.type = "image/*"

        // allowing multiple image to be selected
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_MULTIPLE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            // Get the Image from data
            if (data.clipData != null) {
                val cout = data.clipData!!.itemCount
                if(cout > IMAGE_LIMIT){
                    Toast.makeText(this, "이미지는 최대 3장까지 등록 가능합니다.", Toast.LENGTH_LONG).show()
                    selectImage()
                }else{
                    for (i in 0 until cout) {
                        ++count
                        val imageurl = data.clipData!!.getItemAt(i).uri
                        mUriList.add(imageurl)
                        setImage(imageurl)
                    }
                }
            } else {    // 이미지 1장인경우
                count++
                val imageurl = data.data
                mUriList.add(imageurl!!)
                setImage(imageurl)
            }
        } else {
            Toast.makeText(this, "이미지를 선택하지 않으셨습니다.", Toast.LENGTH_LONG).show()
        }
    }
    private fun setImage(imageUri : Uri){
        val inflater =
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val statLayoutItem = inflater.inflate(R.layout.addimage, null) as LinearLayout
        val addImg: RoundedImageView = statLayoutItem.findViewById(R.id.addImage)
        val delImg =
            statLayoutItem.findViewById<ImageView>(R.id.delImage)
        delImg.setOnClickListener {

            if(mUriList.contains(imageUri)){
                mUriList.remove(imageUri)
                count--
                imageLinear.removeView(statLayoutItem)
                imageTxtCount.text = "$count/3"
            }

        }

        Glide.with(applicationContext)
            .load(imageUri)
            .override(300, 300)
            .fitCenter()
            .into(addImg)
        imageLinear.addView(statLayoutItem)
        imageTxtCount.text = "$count/3"
    }

}