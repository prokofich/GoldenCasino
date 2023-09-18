package com.example.goldencasino.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldencasino.api.Repository
import com.example.goldencasino.model.ResponseText
import com.example.goldencasino.model.ResponseWebView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SplashViewModel:ViewModel() {
    val repo = Repository()
    val webViewUrl: MutableLiveData<Response<ResponseWebView>> = MutableLiveData()

    fun setPostParametersPhone(phone_name:String,locale:String,unique:String){
        viewModelScope.launch(Dispatchers.IO) {
            val responce = repo.setParametersPhone(phone_name, locale, unique)
            withContext(Dispatchers.Main){
                webViewUrl.value = responce
            }
        }
    }
}