package com.example.goldencasino.api

import androidx.annotation.Keep
import com.example.goldencasino.model.ResponseText
import com.example.goldencasino.model.ResponseWebView
import retrofit2.Response

class Repository{

    suspend fun setParametersPhone(phone_name:String,locale:String,unique:String): Response<ResponseWebView> {
        return RetrofitInstance.api.setPostParametersPhone(phone_name, locale, unique)
    }

    suspend fun getTextRules(): Response<ResponseText> {
        return RetrofitInstance.api.getTextRules()
    }

}