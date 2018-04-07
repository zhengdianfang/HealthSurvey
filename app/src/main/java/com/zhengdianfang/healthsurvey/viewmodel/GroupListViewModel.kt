package com.zhengdianfang.healthsurvey.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import com.zhengdianfang.healthsurvey.entities.PartGroup
import com.zhengdianfang.healthsurvey.repository.AppRepository

/**
 * Created by dfgzheng on 06/04/2018.
 */
class GroupListViewModel(application: Application) : AndroidViewModel(application) {
    private val appRepository by lazy { AppRepository() }

    fun getQuestionGroupList(uniqueid: String, org_number: String = ""): LiveData<PartGroup> {
       return appRepository.getQuestionGroupList(uniqueid, org_number)
    }

}