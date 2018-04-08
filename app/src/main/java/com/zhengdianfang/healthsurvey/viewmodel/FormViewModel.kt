package com.zhengdianfang.healthsurvey.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.zhengdianfang.healthsurvey.entities.Form
import com.zhengdianfang.healthsurvey.entities.Header
import com.zhengdianfang.healthsurvey.entities.Request
import com.zhengdianfang.healthsurvey.repository.AppRepository
import java.io.File

/**
 * Created by dfgzheng on 05/04/2018.
 */
class FormViewModel(application: Application) : AndroidViewModel(application) {

    private val appRepository by lazy { AppRepository() }

    fun getUserBase(org_number: String = ""): LiveData<Form> {
        return appRepository.getUserBase(org_number)
    }

    fun checkMechanismCode(org_number: String): LiveData<Boolean> {
        return appRepository.checkMechanismCode(org_number)
    }

    fun submitUserInfo(uniqueid: String, form: Form, org_number: String = ""): LiveData<Boolean> {
        return appRepository.submitUserInfo(Request(Header("", uniqueid,  org_number), form))
    }

    fun getSurveyForm(uniqueid: String, org_number: String, group_id: String): LiveData<Form> {
        return appRepository.getSurveyForm(uniqueid, org_number, group_id)
    }

    fun submitSurveyForm(form: Form, uniqueid: String, org_number: String = ""): LiveData<Boolean> {
        return appRepository.submitSurveyForm(form, uniqueid, org_number)
    }

    fun surveyFinish(uniqueid: String, org_number: String): LiveData<Any> {
        return appRepository.surveyFinish(uniqueid, org_number)
    }

    fun trachDirection(uniqueid: String, org_number: String, track: String): LiveData<Any> {
        return appRepository.trachDirection(uniqueid, org_number, track)

    }

    fun uploadPic(file: File): LiveData<String> {
       return appRepository.uploadPic(file)
    }

}