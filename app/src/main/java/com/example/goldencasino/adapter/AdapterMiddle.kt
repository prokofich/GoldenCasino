package com.example.goldencasino.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.goldencasino.R
import com.example.goldencasino.constant.url_image_bomb
import com.example.goldencasino.constant.url_image_gold
import com.example.goldencasino.constant.url_image_znak_question
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdapterMiddle(private val context: Context,private val intrfc:InterfaceGameOver): RecyclerView.Adapter<AdapterMiddle.MiddleViewHolder>() {

    private var listCash = emptyList<Int>()
    var myCash = 0
    var job:Job = Job()

    class MiddleViewHolder(view: View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiddleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_rv_middle,parent,false)
        return MiddleViewHolder(view)
    }

    override fun onBindViewHolder(holder: MiddleViewHolder, position: Int) {
        val idImage = holder.itemView.findViewById<ImageView>(R.id.id_item_rv_middle_img)
        Glide.with(context)
            .load(url_image_znak_question)
            .centerCrop()
            .into(idImage)
    }

    override fun onViewAttachedToWindow(holder: MiddleViewHolder) {
        super.onViewAttachedToWindow(holder)

        val idImage = holder.itemView.findViewById<ImageView>(R.id.id_item_rv_middle_img)
        val idText = holder.itemView.findViewById<TextView>(R.id.id_item_tv_middle)
        //обработка нажатия на изображение
        holder.itemView.setOnClickListener {
            if(listCash[holder.adapterPosition]!=0){
                //cash
                loadImage(url_image_gold,idImage)
                holder.itemView.isEnabled = false
                idText.text = "${listCash[holder.adapterPosition]}"
                showToast("${listCash[holder.adapterPosition]}$")
                myCash+=listCash[holder.adapterPosition]
                intrfc.addCashInTextView(myCash)
            }else{
                job = CoroutineScope(Dispatchers.Main).launch {
                    //bomb
                    loadImage(url_image_bomb,idImage)
                    holder.itemView.isEnabled = false
                    myCash = 0
                    showToast("you've lost")
                    intrfc.addCashInTextView(myCash)
                    delay(1500)
                    intrfc.gameOver(myCash)
                    job.cancel()
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return listCash.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list:List<Int>){
        listCash = list
        notifyDataSetChanged()
    }

    //функция загрузки изображения
    private fun loadImage(url:String,id: ImageView){
        Glide.with(context)
            .load(url)
            .into(id)
    }

    //функция всплывающего сообщения
    private fun showToast(message:String){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
    }

}