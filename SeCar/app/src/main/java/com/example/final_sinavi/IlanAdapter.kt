package com.example.final_sinavi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class IlanAdapter(private val ilanList: ArrayList<Ilan>) : RecyclerView.Adapter<IlanAdapter.IlanHolder>() {

    private val markaLogolari = mapOf(
        "BMW" to R.drawable.bmw,
        "Mercedes" to R.drawable.mercedes,
        "Audi" to R.drawable.audi,
        "Volkswagen" to R.drawable.volkswagen,
        "Toyota" to R.drawable.toyota,
        "Honda" to R.drawable.honda,
        "Hyundai" to R.drawable.hyundai,
        "Kia" to R.drawable.kia,
        "Ford" to R.drawable.ford
    )

    class IlanHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ilanResim: ImageView = itemView.findViewById(R.id.ilanResim)
        val ilanBaslik: TextView = itemView.findViewById(R.id.ilanBaslik)
        val markaLogo: ImageView = itemView.findViewById(R.id.markaLogo)
        val markaModel: TextView = itemView.findViewById(R.id.markaModel)
        val ilanFiyat: TextView = itemView.findViewById(R.id.ilanFiyat)
        val ilanSahibi: TextView = itemView.findViewById(R.id.ilanSahibi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IlanHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ilan_item, parent, false)
        return IlanHolder(view)
    }

    override fun onBindViewHolder(holder: IlanHolder, position: Int) {
        val ilan = ilanList[position]

        // Resmi yükle
        Picasso.get().load(ilan.resimUrl).into(holder.ilanResim)

        // Başlığı ayarla
        holder.ilanBaslik.text = ilan.baslik

        // Marka logosu ve marka-model bilgisini ayarla
        markaLogolari[ilan.marka]?.let { logoResId ->
            holder.markaLogo.visibility = View.VISIBLE
            holder.markaLogo.setImageResource(logoResId)
        } ?: run {
            // Eğer logo bulunamazsa, logo görünürlüğünü gizle
            holder.markaLogo.visibility = View.GONE
        }
        holder.markaModel.text = "${ilan.marka} ${ilan.model}"

        // Fiyatı formatla ve ayarla
        holder.ilanFiyat.text = "${ilan.fiyat} TL"

        // İlan sahibi bilgisini ayarla
        holder.ilanSahibi.text = ilan.kullaniciEmail
    }

    override fun getItemCount(): Int {
        return ilanList.size
    }

    fun updateIlanList(newIlanList: List<Ilan>) {
        ilanList.clear()
        ilanList.addAll(newIlanList)
        notifyDataSetChanged()
    }
}