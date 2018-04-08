package com.zhengdianfang.healthsurvey.entities

class Version(var newversion: String, var android_url: String,
              var updateInfos: String, var type: String) {
    companion object {
        val MUST = "1"
        val MAYBE = "2"
    }
}