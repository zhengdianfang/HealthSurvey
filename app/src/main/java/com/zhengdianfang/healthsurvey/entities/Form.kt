package com.zhengdianfang.healthsurvey.entities

/**
 * Created by dfgzheng on 05/04/2018.
 */
data class Form(
        var title: String,
        var id: String,
        var required: String,
        var attachment: String,
        var prize_url: String,
        var subdata: MutableList<Question>? = null
) {
    companion object {
        const val NONE_ATTACHMENT = 0
        const val HAVE_ATTACHMENT = 1
    }
}