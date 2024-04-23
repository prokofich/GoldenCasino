package com.example.goldencasino.model.api

import androidx.annotation.Keep
import com.example.goldencasino.model.ResponseText
import com.example.goldencasino.model.ResponseWebView
import retrofit2.Response

class Repository{

    suspend fun setParametersPhone(phoneName : String , locale : String , unique : String) : Response<ResponseWebView> {
        return RetrofitInstance.api.setPostParametersPhone(phoneName , locale , unique)
    }

    suspend fun getTextRules() : Response<ResponseText> {
        return RetrofitInstance.api.getTextRules()
    }

}