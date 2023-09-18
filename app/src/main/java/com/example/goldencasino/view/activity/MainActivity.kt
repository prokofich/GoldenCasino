package com.example.goldencasino.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.goldencasino.R
import com.example.goldencasino.constant.APP_PREFERENCES
import com.example.goldencasino.constant.MAIN
import com.example.goldencasino.constant.MY_CASH

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MAIN = this
        navController = Navigation.findNavController(this,R.id.id_nav_host)

        //установка полноэкранного режима
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

    }

    //функция получения денег
    fun getMyCash(): Int {
        return getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE).getInt(MY_CASH, 0)
    }

    //функция обновления денежного счета
    fun addMyCash(cash:Int){
        val pref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        pref.edit()
            .putInt(MY_CASH,getMyCash()+cash)
            .apply()
    }

    //функция обновления денежного счета
    fun minusMyCash(cash:Int){
        val pref = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        pref.edit()
            .putInt(MY_CASH,getMyCash()-cash)
            .apply()
    }






}