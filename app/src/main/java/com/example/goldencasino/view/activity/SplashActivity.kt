package com.example.goldencasino.view.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.goldencasino.R
import com.example.goldencasino.constant.APP_PREFERENCES
import com.example.goldencasino.constant.ID
import com.example.goldencasino.constant.LAST_DAY
import com.example.goldencasino.constant.url_image_splash
import com.example.goldencasino.databinding.ActivitySplashBinding
import com.example.goldencasino.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import java.util.UUID

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var jobProgress:Job = Job()
    private var jobPercent:Job = Job()
    private var percent = 25

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //загрузка картинки с сервера
        loadImage(url_image_splash,binding.idSplashImg)

        //устновка полноэкранного режима
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        var namePhone = Build.MODEL.toString()
        var locale = Locale.getDefault().getDisplayLanguage().toString()
        var id = ""

        if (getMyId()!=""){
            id = getMyId()
        }else{
            id = UUID.randomUUID().toString()
            setMyId(id)
        }

        splashViewModel.setPostParametersPhone(namePhone,locale,id)

        splashViewModel.webViewUrl.observe(this){ responce ->
            when(responce.body()!!.url){
                "no" -> { goToMainPush() }
                "nopush" -> { goToMainNoPush() }
                else -> { goToWeb(responce.body()!!.url) }
            }
        }

        binding.idSplashProgressBar.max = 2000
        val finishProgress = 2000

        //показ анимации загрузки
        ObjectAnimator.ofInt(binding.idSplashProgressBar,"progress",finishProgress)
            .setDuration(4000)
            .start()

        //показ процентов загрузки
        jobPercent = CoroutineScope(Dispatchers.Main).launch{
            while(percent!=125){
                binding.idSplashTvProgress.text = "$percent%"
                delay(1000)
                percent+=25
            }
            jobPercent.cancel()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(jobPercent.isActive){
            jobPercent.cancel()
        }
        if(jobProgress.isActive){
            jobProgress.cancel()
        }
        finishAffinity()
    }

    //функция загрузки изображения
    private fun loadImage(url:String,id:ImageView){
        Glide.with(this)
            .load(url)
            .centerCrop()
            .into(id)
    }

    //функция получения последнего дня,когда был получен денежный приз
    private fun getLastDay():String{
        val preferences = getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).getString(LAST_DAY,"")
        return preferences ?: ""
    }

    //проверка на переход к получению денежного приза
    private fun checkLastDay(){
        if(LocalDate.now().toString()!=getLastDay()){
            startActivity(Intent(this,FreeMoneyActivity::class.java))
        }else{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    fun getMyId():String{
        var preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString(ID,"")
        return preferences ?: ""
    }

    fun setMyId(id:String){
        var preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        preferences.edit()
            .putString(ID,id)
            .apply()
    }

    fun goToMainPush() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            checkLastDay()
        }
    }

    fun goToMainNoPush() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            checkLastDay()
        }
    }

    fun goToWeb(url:String) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            var intent = Intent(this@SplashActivity,WebViewActivity::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
        }
    }



}