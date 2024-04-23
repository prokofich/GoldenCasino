package com.example.goldencasino.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.goldencasino.model.constant.APP_PREFERENCES
import com.example.goldencasino.model.constant.LAST_DAY
import com.example.goldencasino.model.constant.MY_CASH
import com.example.goldencasino.model.constant.url_image_case_cash
import com.example.goldencasino.model.constant.url_image_cash
import com.example.goldencasino.model.constant.url_image_free_money_activity
import com.example.goldencasino.databinding.ActivityFreeMoneyBinding
import org.threeten.bp.LocalDate

class FreeMoneyActivity : AppCompatActivity() {

    private var binding: ActivityFreeMoneyBinding? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFreeMoneyBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        //загрузка фоновой картинки
        loadImage(url_image_free_money_activity,binding?.idImgFreeMoney)

        //загрузка картинки денег
        loadImage(url_image_cash,binding?.idFreeMoneyIvCash)

        //загрузка картинки кейса с деньгами
        loadImage(url_image_case_cash,binding?.idFreeMoneyImgCase)

        //обработка нажатия на картинку кейса+получение призов
        binding?.idFreeMoneyImgCase?.setOnClickListener {
            it.isVisible = false
            binding?.idFreeMoneyTvComm?.isVisible = false
            val winCash = listOf(500,550,600,650,700,750,800,850,900,950,1000).shuffled()[0]
            addMyCash(winCash)
            setLastDay(LocalDate.now().toString())
            showToast("$winCash$")
            binding?.idFreeMoneyButtonContinue?.isVisible = true
            binding?.idFreeMoneyIvCash?.isVisible = true
            binding?.idFreeMoneyTvCash?.isVisible = true
            binding?.idFreeMoneyTvCash?.text = "$winCash$"
        }

        //переход в меню
        binding?.idFreeMoneyButtonContinue?.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }

    }

    //обработка выхода
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    //функция загрузки изображения
    private fun loadImage(url:String,id: ImageView?){
        id?.let {
            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(it)
        }
    }

    //функция показа всплывающего сообщения
    private fun showToast(message:String) = Toast.makeText(this,message,Toast.LENGTH_SHORT).show()

    //функция получения денег
    private fun getMyCash() : Int {
        return getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE).getInt(MY_CASH, 0)
    }

    //функция обновления денежного счета
    private fun addMyCash(cash : Int){
        getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).edit().putInt(MY_CASH,getMyCash()+cash).apply()
    }

    //функция обновления последнего дня
    private fun setLastDay(day : String){
        getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).edit().putString(LAST_DAY,day).apply()
    }

}