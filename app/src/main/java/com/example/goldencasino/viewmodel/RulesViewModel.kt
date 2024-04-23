package com.example.goldencasino.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldencasino.model.api.Repository
import com.example.goldencasino.model.ResponseText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class RulesViewModel:ViewModel(){

    private val repository = Repository()
    val text: MutableLiveData <Response <ResponseText> > = MutableLiveData()

    fun getTextRules(){
        viewModelScope.launch(Dispatchers.IO) {
            val responce = repository.getTextRules()
            withContext(Dispatchers.Main){
                text.value = responce
            }
        }
    }

}