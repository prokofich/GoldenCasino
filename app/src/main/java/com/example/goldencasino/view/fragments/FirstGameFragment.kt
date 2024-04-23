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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldencasino.R
import com.example.goldencasino.model.adapter.AdapterEasy
import com.example.goldencasino.model.adapter.AdapterHard
import com.example.goldencasino.model.adapter.AdapterMiddle
import com.example.goldencasino.model.adapter.InterfaceGameOver
import com.example.goldencasino.model.constant.COMPLEXITY
import com.example.goldencasino.model.constant.COMPLEXITY_EASY
import com.example.goldencasino.model.constant.COMPLEXITY_HARD
import com.example.goldencasino.model.constant.COMPLEXITY_MIDDLE
import com.example.goldencasino.model.constant.MAIN
import com.example.goldencasino.model.constant.listCashEasy
import com.example.goldencasino.model.constant.listCashHard
import com.example.goldencasino.model.constant.listCashMiddle
import com.example.goldencasino.model.constant.url_image_cash
import com.example.goldencasino.model.constant.url_image_menu_fragment
import com.example.goldencasino.databinding.FragmentFirstGameBinding
import com.example.goldencasino.databinding.FragmentMenuBinding

class FirstGameFragment : Fragment(), InterfaceGameOver {

    private var binding : FragmentFirstGameBinding? = null
    private var recyclerView : RecyclerView? = null
    private var adapterEasy : AdapterEasy? = null
    private var adapterMiddle : AdapterMiddle? = null
    private var adapterHard : AdapterHard? = null
    private var listCashForAdapter = listOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFirstGameBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //загрузка фоновой картинки
        loadImage(url_image_menu_fragment,binding?.idGameImg)

        //загрузка картинки денег
        loadImage(url_image_cash,binding?.idGameIvCashWin)

        recyclerView = binding?.idGameRv

        when(requireArguments().getString(COMPLEXITY)){
            COMPLEXITY_EASY -> {
                adapterEasy = AdapterEasy(requireContext(),this)
                recyclerView?.layoutManager = GridLayoutManager(requireContext(),3)
                recyclerView?.adapter = adapterEasy
                listCashForAdapter = listCashEasy.shuffled()
                adapterEasy?.setList(listCashForAdapter)

            }
            COMPLEXITY_MIDDLE -> {
                adapterMiddle = AdapterMiddle(requireContext(),this)
                recyclerView?.layoutManager = GridLayoutManager(requireContext(),4)
                recyclerView?.adapter = adapterMiddle
                listCashForAdapter = listCashMiddle.shuffled()
                adapterMiddle?.setList(listCashForAdapter)

            }
            COMPLEXITY_HARD -> {
                adapterHard = AdapterHard(requireContext(),this)
                recyclerView?.layoutManager = GridLayoutManager(requireContext(),5)
                recyclerView?.adapter = adapterHard
                listCashForAdapter = listCashHard.shuffled()
                adapterHard?.setList(listCashForAdapter)
            }
        }

        //выход в меню из игры
        binding?.idGameCsFinishButtonMenu?.setOnClickListener {
            MAIN.navController?.navigate(R.id.action_firstGameFragment_to_menuFragment)
        }

        //выход в меню из игры
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            when(requireArguments().getString(COMPLEXITY)){
                COMPLEXITY_EASY   -> { adapterEasy?.job?.cancel()   }
                COMPLEXITY_MIDDLE -> { adapterMiddle?.job?.cancel() }
                COMPLEXITY_HARD   -> { adapterHard?.job?.cancel()   }
            }
            MAIN.navController?.navigate(R.id.action_firstGameFragment_to_menuFragment)
        }

        //закончить и забрать деньги
        binding!!.idGameButtonFinish.setOnClickListener {
            when(requireArguments().getString(COMPLEXITY)){
                COMPLEXITY_EASY -> {
                    if(adapterEasy?.job?.isActive == true) adapterEasy?.job?.cancel()
                    gameOver(adapterEasy?.myCash)
                }
                COMPLEXITY_MIDDLE -> {
                    if(adapterMiddle?.job?.isActive == true) adapterMiddle?.job?.cancel()
                    gameOver(adapterMiddle?.myCash)
                }
                COMPLEXITY_HARD -> {
                    if(adapterHard?.job?.isActive == true) adapterHard?.job?.cancel()
                    gameOver(adapterHard?.myCash)
                }
            }
        }

        //рестарт уровня
        binding!!.idGameCsFinishButtonAgain.setOnClickListener {
            when(requireArguments().getString(COMPLEXITY)){
                COMPLEXITY_EASY -> {
                    if(MAIN.getMyCash() >= 100){
                        listCashForAdapter = listCashEasy.shuffled()
                        adapterEasy?.setList(listCashForAdapter)
                        adapterEasy?.myCash = 0
                        MAIN.minusMyCash(100)
                    }else{
                        MAIN.navController?.navigate(R.id.action_firstGameFragment_to_menuFragment)
                        showToast("you don't have enough funds")
                    }
                }
                COMPLEXITY_MIDDLE -> {
                    if(MAIN.getMyCash() >= 150){
                        listCashForAdapter = listCashMiddle.shuffled()
                        adapterMiddle?.setList(listCashForAdapter)
                        adapterMiddle?.myCash = 0
                        MAIN.minusMyCash(150)
                    }else{
                        MAIN.navController?.navigate(R.id.action_firstGameFragment_to_menuFragment)
                        showToast("you don't have enough funds")
                    }
                }
                COMPLEXITY_HARD -> {
                    if(MAIN.getMyCash() >= 0){
                        listCashForAdapter = listCashHard.shuffled()
                        adapterHard?.setList(listCashForAdapter)
                        adapterHard?.myCash = 0
                        MAIN.minusMyCash(200)
                    }else{
                        MAIN.navController?.navigate(R.id.action_firstGameFragment_to_menuFragment)
                        showToast("you don't have enough funds")
                    }
                }
            }
            binding?.idGameRv?.isVisible = true
            binding?.idGameButtonFinish?.isVisible = true
            binding?.idGameTvCashWin?.isVisible = true
            binding?.idGameIvCashWin?.isVisible = true
            binding?.idGameCsFinish?.isVisible = false
        }

    }

    //функция загрузки изображения
    private fun loadImage(url : String , id : ImageView?){
        id?.let {
            Glide.with(this)
                .load(url)
                .centerCrop()
                .into(it)
        }
    }

    //очистка биндинга при удалении вью
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //функция окончания игры
    @SuppressLint("SetTextI18n")
    override fun gameOver(cashWin : Int?) {
        cashWin?.let {
            MAIN.addMyCash(it)
            binding?.idGameRv?.isVisible = false
            binding?.idGameButtonFinish?.isVisible = false
            binding?.idGameTvCashWin?.isVisible = false
            binding?.idGameIvCashWin?.isVisible = false
            binding?.idGameCsFinish?.isVisible = true
            binding?.idGameCsFinishTvCash?.text = "$it$"
        }
    }

    //функция обновления денежного счета
    @SuppressLint("SetTextI18n")
    override fun addCashInTextView(cashWin : Int) {
        binding?.idGameTvCashWin?.text = "$cashWin$"
    }

    //функция всплывающего сообщения
    private fun showToast(message : String) = Toast.makeText(requireContext() , message , Toast.LENGTH_SHORT).show()

}