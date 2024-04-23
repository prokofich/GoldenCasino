package com.example.goldencasino.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.goldencasino.R
import com.example.goldencasino.model.constant.MAIN
import com.example.goldencasino.model.constant.url_image_fragment_rules
import com.example.goldencasino.databinding.FragmentRulesBinding
import com.example.goldencasino.viewmodel.RulesViewModel

class RulesFragment : Fragment() {

    private var binding : FragmentRulesBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRulesBinding.inflate(inflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //загрузка фоновой картинки
        loadImage(url_image_fragment_rules,binding?.idIvRules)

        //выход обратно в меню
        binding?.idButtonBack?.setOnClickListener {
            MAIN.navController?.navigate(R.id.action_rulesFragment_to_menuFragment)
        }

        val rulesViewModel = ViewModelProvider(this)[RulesViewModel::class.java]

        rulesViewModel.getTextRules()

        rulesViewModel.text.observe(viewLifecycleOwner){
            binding!!.idTvRules.text = it.body()!!.text
        }

        //выход из игры
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            MAIN.navController?.navigate(R.id.action_rulesFragment_to_menuFragment)
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

}