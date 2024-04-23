package com.example.goldencasino.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.goldencasino.R
import com.example.goldencasino.model.constant.APP_PREFERENCES
import com.example.goldencasino.model.constant.MAIN
import com.example.goldencasino.model.constant.MY_CASH

class MainActivity : AppCompatActivity() {

    var navController : NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MAIN = this
        navController = Navigation.findNavController(this,R.id.id_nav_host)

    }

    //функция получения денег
    fun getMyCash(): Int {
        return getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE).getInt(MY_CASH, 0)
    }

    //функция обновления денежного счета
    fun addMyCash(cash:Int){
        getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(MY_CASH,getMyCash()+cash).apply()
    }

    //функция обновления денежного счета
    fun minusMyCash(cash:Int){
        getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().putInt(MY_CASH,getMyCash()-cash).apply()
    }

}