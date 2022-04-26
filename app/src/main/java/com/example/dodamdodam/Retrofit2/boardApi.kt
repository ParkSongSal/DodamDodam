package com.example.dodamdodam.Retrofit2

import com.example.dodamdodam.Notice.ResultNotice
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface boardApi {

    /* 소개문 내용*/
    @Multipart
    @POST("getIntroduce.php")
    fun getIntroduce(@Part("boardGubun") boardGubun : RequestBody) : Call<ResultModel>


    /* 소개문 등록*/
    @Multipart
    @POST("introduce_insert.php")
    fun getIntroduceInsert(@Part("boardGubun") boardGubun : RequestBody,
                           @Part("boardContent") boardContent : RequestBody,
                           @Part("insertId") insertId : RequestBody,
                           @Part("insertDate") insertDate : RequestBody,
                           @Part("updateId") updateId : RequestBody,
                           @Part("updateDate") updateDate : RequestBody) : Call<ResultModel>

    /* 소개문 수정*/
    @Multipart
    @POST("introduce_modify.php")
    fun getIntroduceModify(@Part("boardGubun") boardGubun : RequestBody,
                           @Part("boardContent") boardContent : RequestBody,
                           @Part("updateId") updateId : RequestBody,
                           @Part("updateDate") updateDate : RequestBody) : Call<ResultModel>


    /* 소개문 등록 / 수정 여부  */
    @Multipart
    @POST("introduce_validate.php")
    fun getIntroduceValidate(@Part("boardGubun") boardGubun: RequestBody) : Call<ResultModel>


    /* 소개문 List*/
    @Multipart
    @POST("introduceList.php")
    fun getIntroduceList(@Part("boardGubun") boardGubun: RequestBody) : Call<List<ResultIntroduce>>



    /* 공지사항 목록 */
    @POST("getNoticeList.php")
    fun getNoticeList() : Call<List<ResultNotice>>


    /* 공지사항 등록 */
    @Multipart
    @POST("notice_insert.php")
    fun NoticeInsert(@Part("title") title : RequestBody,
                     @Part("content") content : RequestBody,
                     @Part("insertId") insertId : RequestBody,
                     @Part("insertDate") insertDate : RequestBody,
                     @Part("updateId") updateId : RequestBody,
                     @Part("updateDate") updateDate : RequestBody) : Call<ResultNotice>

    /* 공지사항 수정*/
    @Multipart
    @POST("notice_modify.php")
    fun NoticeUpdate(@Part("seq") seq : RequestBody,
                     @Part("title") title : RequestBody,
                     @Part("content") content : RequestBody,
                     @Part("updateId") updateId : RequestBody,
                     @Part("updateDate") updateDate : RequestBody) : Call<ResultNotice>

    /* 공지사항 수정*/
    @Multipart
    @POST("notice_delete.php")
    fun NoticeDelete(@Part("seq") seq : RequestBody) : Call<ResultNotice>


}