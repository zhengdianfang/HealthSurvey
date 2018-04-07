package com.zhengdianfang.healthsurvey.views

import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.zhengdianfang.healthsurvey.BuildConfig
import com.zhengdianfang.healthsurvey.Des4
import com.zhengdianfang.healthsurvey.R
import com.zhengdianfang.healthsurvey.datasource.cloud.WebService
import com.zhengdianfang.healthsurvey.entities.Response
import com.zhengdianfang.healthsurvey.entities.SmsCode
import com.zhengdianfang.healthsurvey.repository.AppRepository
import org.jetbrains.anko.runOnUiThread
import retrofit2.Call
import retrofit2.Callback


/**
 * Created by dfgzheng on 05/04/2018.
 */
class SMSCodeButton(context: Context?, attrs: AttributeSet?) : Button(context, attrs), View.OnClickListener {


    private val appRepository by lazy { AppRepository() }
    private var nowSmsCode = ""
    var getPhone: () -> String = { "" }

    init {
        setOnClickListener(this)
        activeStyle()
        setPadding(16, 8, 16,8)
    }

    private val timer = object : CountDownTimer(60000, 1000) {
        //根据间隔时间来不断回调此方法，这里是每隔1000ms调用一次
        override fun onTick(millisUntilFinished: Long) {
            context?.runOnUiThread {
                text = resources.getString(R.string.timer_label, millisUntilFinished / 1000)
            }
        }

        //结束倒计时调用
        override fun onFinish() {
            activeStyle()
        }
    }

    private fun activeStyle() {
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary))
        setText(R.string.get_sms_code)
        setTextColor(Color.WHITE)
        this.isEnabled = true
    }

    private fun unActiveStyle() {
        setBackgroundColor(Color.LTGRAY)
        text = resources.getString(R.string.timer_label, 60)
        setTextColor(Color.BLACK)
        this.isEnabled = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(resources.getDimension(R.dimen.smsCodeButtonWidth).toInt()
                , resources.getDimension(R.dimen.btnHeight).toInt())
    }

    override fun onClick(p0: View?) {
        val phone = getPhone()
        if (!TextUtils.isEmpty(phone)) {
            unActiveStyle()
            timer.start()
            val encode = Des4.encode(phone)
            appRepository.getSMSCode(encode, object : Callback<Response<SmsCode>> {
                override fun onFailure(call: Call<Response<SmsCode>>?, t: Throwable?) {
                    t?.printStackTrace()
                    Toast.makeText(context, R.string.get_sms_code_failure, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<Response<SmsCode>>?, response: retrofit2.Response<Response<SmsCode>>?) {
                    nowSmsCode = response?.body()?.data?.code ?: ""

                    if (BuildConfig.DEBUG) {
                        Log.d("SMSCodeButton", Des4.decode(nowSmsCode))
                    }
                    if (response?.body()?.status == WebService.HTTP_OK) {
                        Toast.makeText(context, R.string.get_sms_code_success, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, R.string.get_sms_code_failure, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        } else {
            Toast.makeText(context, R.string.please_input_phonenumber, Toast.LENGTH_SHORT).show()
        }
    }

    fun getSmsCode(): String {
        return if (TextUtils.isEmpty(nowSmsCode)) "" else Des4.decode(nowSmsCode)
    }


}
