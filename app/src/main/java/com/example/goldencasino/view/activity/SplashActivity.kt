package com.example.goldencasino.view.activity

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.goldencasino.model.constant.APP_PREFERENCES
import com.example.goldencasino.model.constant.ID
import com.example.goldencasino.model.constant.LAST_DAY
import com.example.goldencasino.model.constant.url_image_splash
import com.example.goldencasino.databinding.ActivitySplashBinding
import com.example.goldencasino.viewmodel.SplashViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import java.util.Locale
import java.util.UUID

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private var binding : ActivitySplashBinding? = null
    private var jobProgress : Job = Job()
    private var jobPercent : Job = Job()
    private var percent = 25

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding?.root
        setContentView(view)

        //загрузка картинки с сервера
        loadImage(url_image_splash,binding?.idSplashImg)

        val splashViewModel = ViewModelProvider(this)[SplashViewModel::class.java]
        val namePhone = Build.MODEL.toString()
        val locale = Locale.getDefault().displayLanguage.toString()
        var id = ""

        if (getMyId().isNotEmpty()){
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

        binding?.idSplashProgressBar?.max = 2000
        val finishProgress = 2000

        //показ анимации загрузки
        ObjectAnimator.ofInt(binding?.idSplashProgressBar,"progress",finishProgress)
            .setDuration(4000)
            .start()

        //показ процентов загрузки
        jobPercent = CoroutineScope(Dispatchers.Main).launch{
            while(percent!=125){
                binding?.idSplashTvProgress?.text = "$percent%"
                delay(1000)
                percent+=25
            }
            jobPercent.cancel()
        }

    }

    @Deprecated("Deprecated in Java")
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
    private fun loadImage(url:String,id:ImageView?){
        id?.let {
            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(it)
        }
    }

    //функция получения последнего дня,когда был получен денежный приз
    private fun getLastDay() : String{
        return getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE).getString(LAST_DAY,"").toString()
    }

    //проверка на переход к получению денежного приза
    private fun checkLastDay(){
        if(LocalDate.now().toString()!=getLastDay()){
            startActivity(Intent(this,FreeMoneyActivity::class.java))
        }else{
            startActivity(Intent(this,MainActivity::class.java))
        }
    }

    private fun getMyId() : String{
        return getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString(ID,"").toString()
    }

    private fun setMyId(id : String){
        getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).edit().putString(ID,id).apply()
    }

    private fun goToMainPush() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            checkLastDay()
        }
    }

    private fun goToMainNoPush() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            checkLastDay()
        }
    }

    private fun goToWeb(url : String) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(4000)
            val intent = Intent(this@SplashActivity,WebViewActivity::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
        }
    }

}