package com.zhengdianfang.healthsurvey.entities

/**
 * Created by dfgzheng on 05/04/2018.
 */
data class Condition(var checkType: String,
                     var weight: String,
                     var required: String,
                     var unit: String) {
    companion object {
        const val NONE_CONDITION = 0
        const val NUMBER_TYPE = 1
        const val PHONENUMBER_TYPE = 2
        const val IDCARD_TYPE = 3

    }
}