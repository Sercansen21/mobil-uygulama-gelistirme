package com.example.final_sinavi

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ArabaAdapter(private var arabaListesi: List<Araba>) :
    RecyclerView.Adapter<ArabaAdapter.ArabaViewHolder>() {


    class ArabaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val baslikText: TextView = view.findViewById(R.id.baslikText)
        val markaModelText: TextView = view.findViewById(R.id.markaModelText)
        val fiyatText: TextView = view.findViewById(R.id.fiyatText)
        val kullaniciText: TextView = view.findViewById(R.id.kullaniciText)
        val markaLogo: ImageView = view.findViewById(R.id.markaLogo)
    }


    fun updateList(newList: List<Araba>) {
        arabaListesi = newList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArabaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.araba_item, parent, false)
        return ArabaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArabaViewHolder, position: Int) {
        val araba = arabaListesi[position]

        holder.baslikText.text = araba.baslik
        holder.markaModelText.text = "${araba.marka} ${araba.model}"
        holder.fiyatText.text = "${araba.fiyat} TL"
        holder.kullaniciText.text = "İlan Sahibi: ${araba.kullaniciEmail?.substringBefore('@')}"


        val logoResId = when (araba.marka.lowercase()) {
            "bmw" -> R.drawable.bmw
            "mercedes" -> R.drawable.mercedes
            "audi" -> R.drawable.audi
            "volkswagen" -> R.drawable.volkswagen
            "toyota" -> R.drawable.toyota
            "honda" -> R.drawable.honda
            "hyundai" -> R.drawable.hyundai
            "kia" -> R.drawable.kia
            "ford" -> R.drawable.ford
            else -> R.drawable.ic_launcher_foreground
        }
        holder.markaLogo.setImageResource(logoResId)


        if (araba.resimUrl.isNotEmpty()) {
            Glide.with(holder.imageView.context)
                .load(araba.resimUrl)
                .centerCrop()
                .into(holder.imageView)
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, IlanDetayActivity::class.java)
            intent.putExtra("araba", araba)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount() = arabaListesi.size
}