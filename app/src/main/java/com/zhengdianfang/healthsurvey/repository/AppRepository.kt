package com.zhengdianfang.healthsurvey.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.zhengdianfang.healthsurvey.datasource.cloud.WebService
import com.zhengdianfang.healthsurvey.entities.*
import retrofit2.Call
import retrofit2.Callback


/**
 * Created by dfgzheng on 05/04/2018.
 */
class AppRepository {

    fun getProducts(): LiveData<MutableList<Product>> {
        val data = MutableLiveData<MutableList<Product>>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .getProducts(WebService.gson.toJson(Request(null, null)))
                .enqueue(object : Callback<Response<MutableList<Product>>> {
                    override fun onFailure(call: Call<Response<MutableList<Product>>>?, t: Throwable?) {
                    }

                    override fun onResponse(call: Call<Response<MutableList<Product>>>?,
                                            response: retrofit2.Response<Response<MutableList<Product>>>?) {
                        val body = response?.body()
                        if (body?.status == WebService.HTTP_OK) {
                            data.value = body.data
                        }
                    }
                })

        return data
    }

    fun getDescription(): LiveData<String> {
        val data = MutableLiveData<String>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .getHomeDescription()
                .enqueue(object : Callback<Response<Description>> {
                    override fun onFailure(call: Call<Response<Description>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Description>>?,
                                            response: retrofit2.Response<Response<Description>>?) {
                        val body = response?.body()
                        Log.d("AppRepository", body?.data.toString())
                        if (body?.status == WebService.HTTP_OK) {
                            data.value = body.data?.desc ?: ""
                        }
                    }
                })
        return data
    }

    fun getUserBase(org_number: String): LiveData<Form> {
        val data = MutableLiveData<Form>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .getUserBase(WebService.gson.toJson(Request(Header("", "", org_number), null)))
                .enqueue(object : Callback<Response<Form>> {
                    override fun onFailure(call: Call<Response<Form>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Form>>?, response: retrofit2.Response<Response<Form>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        if (body?.status == WebService.HTTP_OK) {
                            data.value = body.data
                        }
                    }

                })
        return data

    }

    fun getSMSCode(phoneNumber: String, callback: Callback<Response<SmsCode>>) {
        WebService.retrofit
                .create(WebService.Api::class.java)
                .getSMSCode(WebService.gson.toJson(Request(Header(phoneNumber), null)))
                .enqueue(callback)
    }

    fun checkMechanismCode(org_number: String): LiveData<Boolean> {

        val data = MutableLiveData<Boolean>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .checkMechanismCode(WebService.gson.toJson(Request(Header("", "", org_number), null)))
                .enqueue(object : Callback<Response<Any>> {
                    override fun onFailure(call: Call<Response<Any>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Any>>?, response: retrofit2.Response<Response<Any>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        data.value = body?.status == WebService.HTTP_OK
                    }

                })
        return data
    }

    fun submitUserInfo(request: Request): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .submitUserInfo(WebService.gson.toJson(request))
                .enqueue(object : Callback<Response<Any>> {
                    override fun onFailure(call: Call<Response<Any>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Any>>?, response: retrofit2.Response<Response<Any>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        data.value = body?.status == WebService.HTTP_OK
                    }

                })
        return data
    }

    fun getQuestionGroupList(uniqueid: String, org_number: String = ""): LiveData<PartGroup> {
        val data = MutableLiveData<PartGroup>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .getQuestionGroupList(WebService.gson.toJson(Request(Header("", uniqueid, org_number), null)))
                .enqueue(object : Callback<Response<PartGroup>> {
                    override fun onFailure(call: Call<Response<PartGroup>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<PartGroup>>?, response: retrofit2.Response<Response<PartGroup>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        if (body?.status == WebService.HTTP_OK) {
                            data.value = body?.data
                        }
                    }

                })
        return data
    }

    fun getSurveyForm(uniqueid: String, org_number: String, group_id: String): LiveData<Form> {
        val data = MutableLiveData<Form>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .getSurveyForm(WebService.gson.toJson(Request(Header("", uniqueid, org_number), GroupId(group_id))))
                .enqueue(object : Callback<Response<Form>> {
                    override fun onFailure(call: Call<Response<Form>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Form>>?, response: retrofit2.Response<Response<Form>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        if (body?.status == WebService.HTTP_OK) {
                            data.value = body?.data
                        }
                    }

                })
        return data
    }


    fun submitSurveyForm(form: Form, uniqueid: String, org_number: String = ""): LiveData<Boolean> {
        val data = MutableLiveData<Boolean>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .submitSurveyForm(WebService.gson.toJson(Request(Header("", uniqueid, org_number), form)))
                .enqueue(object : Callback<Response<Any>> {
                    override fun onFailure(call: Call<Response<Any>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Any>>?, response: retrofit2.Response<Response<Any>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        data.value = body?.status == WebService.HTTP_OK
                    }

                })
        return data
    }

    fun surveyFinish(uniqueid: String, org_number: String): LiveData<Any> {
        val data = MutableLiveData<Any>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .surveyFinish(WebService.gson.toJson(Request(Header("", uniqueid, org_number), null)))
                .enqueue(object : Callback<Response<Any>> {
                    override fun onFailure(call: Call<Response<Any>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Any>>?, response: retrofit2.Response<Response<Any>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        data.value = body?.data
                    }

                })
        return data
    }

    fun trachDirection(uniqueid: String, org_number: String, track: String): LiveData<Any> {
        val data = MutableLiveData<Any>()
        WebService.retrofit
                .create(WebService.Api::class.java)
                .trachDirection(WebService.gson.toJson(Request(Header("", uniqueid, org_number, track), null)))
                .enqueue(object : Callback<Response<Any>> {
                    override fun onFailure(call: Call<Response<Any>>?, t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onResponse(call: Call<Response<Any>>?, response: retrofit2.Response<Response<Any>>?) {
                        Log.d("AppRepository", response?.message())
                        val body = response?.body()
                        data.value = body?.data
                    }

                })
        return data
    }

}