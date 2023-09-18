package com.example.goldencasino.api

import com.example.goldencasino.model.ResponseText
import com.example.goldencasino.model.ResponseWebView
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("splash.php")
    suspend fun setPostParametersPhone(
        @Field("phone_name") phone_name:String,
        @Field("locale") locale:String,
        @Field("unique") unique:String
    ): Response<ResponseWebView>

    @GET("20/TextRules.json")
    suspend fun getTextRules():Response<ResponseText>

}