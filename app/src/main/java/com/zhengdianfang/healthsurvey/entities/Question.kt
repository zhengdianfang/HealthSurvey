package com.zhengdianfang.healthsurvey.entities

import com.google.gson.annotations.SerializedName

/**
 * Created by dfgzheng on 05/04/2018.
 */
class Question (
        var qid: String,
        var title: String,
        var type: String,
        var code: String,
        var pic: String,
        var isComparisonBeforeEat: String,
        var options: MutableList<Option>?,
        @SerializedName("ext") var condition: Condition?,
        @SerializedName("answers") var answers: Answer
) {

    fun parseType(): Int{
        if (type == INPUT.toString()) {
            when(condition?.checkType){
                Condition.PHONENUMBER_TYPE.toString() ->
                    return PHONE_INPUT_ELECTION
                Condition.IDCARD_TYPE.toString() ->
                    return IDCARD_INPUT_ELECTION
            }
            if (code.toUpperCase() == "B11") {
               return COMPANY_ELECTION
            }
            if (code.toUpperCase() == "B_V2_GX") {
                return FUNC_ELECTION
            }
        }
        return type.toInt()
    }

    companion object {
        val SINGLE_ELECTION = 1
        val MULTI_ELECTION= 2
        val INPUT = 3
        val AUTOCOMPLETE = 4
        val AUTO_FILL = 5
        val DATE = 6
        val ADDRESS = 7
        val PHONE_INPUT_ELECTION = 11
        val IDCARD_INPUT_ELECTION = 12
        val COMPANY_ELECTION = 13
        val FUNC_ELECTION = 14
    }


}