package com.example.final_sinavi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView

class MarkaAdapter(context: Context) : ArrayAdapter<Marka>(context, 0) {

    private val tumMarkalar = listOf(
        Marka("BMW", R.drawable.bmw),
        Marka("Mercedes", R.drawable.mercedes),
        Marka("Audi", R.drawable.audi),
        Marka("Volkswagen", R.drawable.volkswagen),
        Marka("Toyota", R.drawable.toyota),
        Marka("Honda", R.drawable.honda),
        Marka("Hyundai", R.drawable.hyundai),
        Marka("Kia", R.drawable.kia),
        Marka("Ford", R.drawable.ford)
    )

    private var filtrelenmisMarkalar = tumMarkalar

    override fun getCount(): Int = filtrelenmisMarkalar.size

    override fun getItem(position: Int): Marka = filtrelenmisMarkalar[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.marka_item, parent, false)

        val marka = getItem(position)
        val logo = view.findViewById<ImageView>(R.id.markaLogo)
        val text = view.findViewById<TextView>(R.id.markaAdi)

        logo.setImageResource(marka.logoResId)
        text.text = marka.ad

        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint == null || constraint.isEmpty()) {
                    filterResults.values = tumMarkalar
                    filterResults.count = tumMarkalar.size
                } else {
                    val filtreliListe = tumMarkalar.filter {
                        it.ad.lowercase().contains(constraint.toString().lowercase())
                    }
                    filterResults.values = filtreliListe
                    filterResults.count = filtreliListe.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filtrelenmisMarkalar = if (results?.values == null) {
                    ArrayList()
                } else {
                    @Suppress("UNCHECKED_CAST")
                    results.values as List<Marka>
                }
                notifyDataSetChanged()
            }
        }
    }
}