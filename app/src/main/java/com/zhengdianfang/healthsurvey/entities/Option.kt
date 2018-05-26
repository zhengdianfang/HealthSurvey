package com.zhengdianfang.healthsurvey.entities

/**
 * Created by dfgzheng on 05/04/2018.
 */
data class Option(var name: String, var weight: String, var omit: String, var more: String, var advise: String) {
     fun isMore(): Boolean {
         return more == "1"
     }
}