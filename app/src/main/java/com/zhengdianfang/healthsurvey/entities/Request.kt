package com.zhengdianfang.healthsurvey.entities

/**
 * Created by dfgzheng on 05/04/2018.
 */

data class Header(var udid: String, var uniqueid: String, var org_number: String, var track_direction: String = "") {
    var phone = ""
    constructor(phone: String) : this( "", "", "") {
        this.phone = phone
    }

}

data class Request(var header: Header?, var data: Any?)

data class SmsCode(var code: String)

data class GroupId(var group_id: String)
