package com.example.futbol_arayuz

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Myadapter(val playerList: ArrayList<Player>) : RecyclerView.Adapter<Myadapter.MyViewHolder>() {
    class MyViewHolder(itemview:View): RecyclerView.ViewHolder(itemview){
        val isim: TextView = itemView.findViewById(R.id.textView)
        val pozisyon: TextView = itemView.findViewById(R.id.textView2)
        val numara: TextView = itemView.findViewById(R.id.textView3)
        val resim: ImageView = itemView.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.rcycel_row,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playerList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentPlayer = playerList[position]

        holder.apply {
            isim.text = currentPlayer.isim
            numara.text = currentPlayer.forma_numarasi.toString()
            pozisyon.text = currentPlayer.pozisyon
            resim.setImageResource(currentPlayer.resim)
            holder.itemView.setOnClickListener {
                var intent = Intent(holder.itemView.context, detay::class.java)
                intent.putExtra("isim", currentPlayer.isim)
                intent.putExtra("pozisyon", currentPlayer.pozisyon)
                intent.putExtra("resim", playerList[position].resim)
                intent.putExtra("forma_numarasi", currentPlayer.forma_numarasi)
                intent.putExtra("takim", currentPlayer.takim)
                intent.putExtra("oynadigi_oyun", currentPlayer.oynadigi_oyun)
                intent.putExtra("gol_sayisi", currentPlayer.gol_sayisi)
                intent.putExtra("asist_sayisi", currentPlayer.asist_sayisi)
                intent.putExtra("yas", currentPlayer.yas)

                holder.itemView.context.startActivity(intent)
            }
        }

    }

}