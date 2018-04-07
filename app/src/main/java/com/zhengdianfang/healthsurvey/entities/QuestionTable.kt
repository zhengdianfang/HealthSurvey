package com.zhengdianfang.healthsurvey.entities

import io.realm.RealmModel
import io.realm.RealmObject

/**
 * Created by dfgzheng on 07/04/2018.
 */
class QuestionTable: RealmModel {
    var qid: String = ""
    var question: String = ""

}