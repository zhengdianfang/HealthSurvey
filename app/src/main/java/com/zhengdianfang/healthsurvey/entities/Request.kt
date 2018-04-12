package com.zhengdianfang.healthsurvey.entities

import android.os.Build
import java.net.NetworkInterface
import java.net.SocketException

/**
 * Created by dfgzheng on 05/04/2018.
 */

data class Header(var udid: String, var uniqueid: String, var org_number: String, var track_direction: String = "") {
    var phone = ""
    constructor(phone: String) : this( "", "", "") {
        this.phone = phone
    }

}

data class Request(var header: Any?, var data: Any?)

data class SmsCode(var code: String)

data class GroupId(var group_id: String)


data class DeviceHeader(var uniqueid: String, var screenheight:Int, var screenwidth: Int,
                        var version: String, var versioncode: Int) {
    var mac = getMacAddress()
    var ostype = "Android"
    var osversion = Build.VERSION.SDK
    var phonetype = Build.MODEL

    fun getMacAddress(): String {
        var macAddress: String? = null
        val buf = StringBuffer()
        var networkInterface: NetworkInterface? = null
        try {
            networkInterface = NetworkInterface.getByName("eth1")
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0")
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02"
            }
            val addr = networkInterface!!.getHardwareAddress()
            for (b in addr) {
                buf.append(String.format("%02X:", b))
            }
            if (buf.length > 0) {
                buf.deleteCharAt(buf.length - 1)
            }
            macAddress = buf.toString()
        } catch (e: SocketException) {
            e.printStackTrace()
            return "02:00:00:00:00:02"
        }

        return macAddress
    }

}