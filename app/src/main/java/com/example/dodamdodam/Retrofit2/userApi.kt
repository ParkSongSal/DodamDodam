package com.example.dodamdodam.Retrofit2

import com.example.dodamdodam.Visit.ResultVisit
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface userApi {

    /* 로그인 */
    @POST("user_login.php")
    fun userLogin(@Query("userId") userId : String,
                  @Query("userPw") userPw : String) : Call<ResultModel>

    /* id 중복검사 */
    @GET("user_id_validate.php")
    fun userIdValidate(@Query("userId") userId : String) : Call<ResultModel>


    /* 회원가입 */
    @GET("user_register.php")
    fun userRegister(@Query("userId") userId : String,
                     @Query("userPw") userPw: String,
                     @Query("userName") userName : String,
                     @Query("userPhone") userPhone : String,
                     @Query("babyName") babyName : String,
                     @Query("babyNum") babyNum : String,
                     @Query("babyBirthDate") babyBirthDate : String,
                     @Query("babyBirthTime") babyBirthTime : String,
                     @Query("babyRelation") babyRelation : String,
                     @Query("regDate") regDate : String,
                     @Query("userAuth") userAuth : String) : Call<ResultModel>


    /* 메인 아기 정보 */
    @Multipart
    @POST("mainBabyInfo.php")
    fun mainBabyInfo(@Part("loginId") loginId : RequestBody) : Call<List<ResultVisit>>

    /* 회원 정보 */
    @Multipart
    @POST("getUserInfo.php")
    fun getUserInfo(@Part("loginId") loginId : RequestBody) : Call<List<ResultVisit>>


    /* 회원정보 수정 */
    @GET("userInfoUpdate.php")
    fun userUpdate(@Query("userId") userId : String,
                     @Query("userPw") userPw: String,
                     @Query("userName") userName : String,
                     @Query("userPhone") userPhone : String,
                     @Query("babyName") babyName : String,
                     @Query("babyNum") babyNum : String,
                     @Query("babyBirthDate") babyBirthDate : String,
                     @Query("babyBirthTime") babyBirthTime : String,
                     @Query("regDate") regDate : String) : Call<ResultModel>

}