package com.example.goldencasino.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.goldencasino.R
import com.example.goldencasino.model.constant.COMPLEXITY
import com.example.goldencasino.model.constant.COMPLEXITY_EASY
import com.example.goldencasino.model.constant.COMPLEXITY_HARD
import com.example.goldencasino.model.constant.COMPLEXITY_MIDDLE
import com.example.goldencasino.model.constant.MAIN
import com.example.goldencasino.model.constant.url_image_cash
import com.example.goldencasino.model.constant.url_image_menu_fragment
import com.example.goldencasino.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var binding : FragmentMenuBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater,container,false)
        return binding?.root
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //показ количества денег
        binding?.idMenuTvTitleCash?.text = "${MAIN.getMyCash()}$"
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //загрузка картинок с сервера
        loadImage(url_image_menu_fragment,binding?.idMenuImg)
        loadImage(url_image_cash,binding?.idMenuIvTitleCash)

        //переход к показу правил игры
        binding?.idMenuTvRules?.setOnClickListener {
            MAIN.navController?.navigate(R.id.action_menuFragment_to_rulesFragment)
        }

        //выход из игры
        binding?.idMenuButtonExit?.setOnClickListener {
            showExitDialog()
        }

        //выход из игры
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            showExitDialog()
        }

        //переход к простому уровню
        binding?.idButtonStartEasy?.setOnClickListener {
            goToGameFragment(100)
        }

        //переход к среднему уровню
        binding?.idButtonStartMiddle?.setOnClickListener {
            goToGameFragment(150)
        }

        //переход к сложному уровню
        binding?.idButtonStartHard?.setOnClickListener {
            goToGameFragment(200)
        }

    }

    //очистка биндинга при удалении вью
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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

    //функция показа диалогового сообщения
    private fun showExitDialog() {
        val options = arrayOf("exit", "cancel")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("do you want to get out?")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    MAIN.finishAffinity()
                }
                1 -> {
                    dialog.cancel()
                }
            }
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    //функция всплывающего сообщения
    private fun showToast(message:String) = Toast.makeText(requireContext(),message,Toast.LENGTH_SHORT).show()

    //переход к игре с проверкой на наличие средств
    private fun goToGameFragment(price:Int){
        when(price){
            100 -> {
                if(MAIN.getMyCash() >= price){
                    MAIN.minusMyCash(100)
                    val bundle = Bundle()
                    bundle.putString(COMPLEXITY, COMPLEXITY_EASY)
                    MAIN.navController?.navigate(R.id.action_menuFragment_to_firstGameFragment,bundle)
                }else{
                    showToast("you don't have enough funds")
                }
            }
            150 -> {
                if(MAIN.getMyCash() >= price){
                    MAIN.minusMyCash(150)
                    val bundle = Bundle()
                    bundle.putString(COMPLEXITY, COMPLEXITY_MIDDLE)
                    MAIN.navController?.navigate(R.id.action_menuFragment_to_firstGameFragment,bundle)
                }else{
                    showToast("you don't have enough funds")
                }
            }
            200 -> {
                if(MAIN.getMyCash() >= price){
                    MAIN.minusMyCash(200)
                    val bundle = Bundle()
                    bundle.putString(COMPLEXITY, COMPLEXITY_HARD)
                    MAIN.navController?.navigate(R.id.action_menuFragment_to_firstGameFragment,bundle)
                }else{
                    showToast("you don't have enough funds")
                }
            }
        }
    }


}