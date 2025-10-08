package com.example.final_sinavi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Araba(
    val baslik: String = "",
    val marka: String = "",
    val model: String = "",
    val fiyat: String = "",
    val resimUrl: String = "",
    val kullaniciEmail: String? = null,
    val tarih: Long = 0
) : Parcelable
