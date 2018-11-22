package com.zhengdianfang.healthsurvey.datasource.cloud

import com.google.gson.GsonBuilder
import com.zhengdianfang.healthsurvey.entities.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Part


/**
 * Created by dfgzheng on 05/04/2018.
 */
class WebService {
    companion object {
        private const val TAG = "WebService"
        private const val HOST = "https://www.xinruihy.cn/"
        val gson by lazy { GsonBuilder().enableComplexMapKeySerialization().create() }
        private val okHttpClient by lazy {
            OkHttpClient.Builder()
                    .build()
        }
        val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(HOST)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
        }

        const val HTTP_OK = 0
    }


    interface Api {
        @FormUrlEncoded
        @POST("api/product_list")
        fun getProducts(@Field("data") request: String): Call<Response<MutableList<Product>>>

        @GET("api/luck_draw")
        fun getHomeDescription(): Call<Response<Description>>


        @FormUrlEncoded
        @POST("api/user_base")
        fun getUserBase(@Field("data") request: String): Call<Response<Form>>

        @FormUrlEncoded
        @POST("api/sendvno")
        fun getSMSCode(@Field("data") request: String): Call<Response<SmsCode>>

        @Multipart
        @POST("api/upload_pic")
        fun uploadPic(@Part file: MultipartBody.Part): Call<Response<Pic>>

        @FormUrlEncoded
        @POST("api/check_org_number")
        fun checkMechanismCode(@Field("data") request: String): Call<Response<Any>>

        @FormUrlEncoded
        @POST("api/user_base_submit")
        fun submitUserInfo(@Field("data") request: String): Call<Response<Any>>

        @FormUrlEncoded
        @POST("api/question_list")
        fun getQuestionGroupList(@Field("data") request: String): Call<Response<PartGroup>>


        @FormUrlEncoded
        @POST("api/group_show")
        fun getSurveyForm(@Field("data") request: String): Call<Response<Form>>

        @FormUrlEncoded
        @POST("api/group_submit")
        fun submitSurveyForm(@Field("data") request: String): Call<Response<Any>>


        @FormUrlEncoded
        @POST("api/submit_finish")
        fun surveyFinish(@Field("data") request: String): Call<Response<Any>>

        @FormUrlEncoded
        @POST("api/track_direction")
        fun trachDirection(@Field("data") request: String): Call<Response<Any>>

        @GET("api/get_version")
        fun upgradleApp(): Call<Response<Version>>

        @FormUrlEncoded
        @POST("api/report_info")
        fun uploadDeviceInfo(@Field("data") request: String): Call<Response<Any>>

        @FormUrlEncoded
        @POST("api/product_nouse")
        fun postNotSatisfiedData(@Field("data") request: String): Call<Response<Any>>
    }
}