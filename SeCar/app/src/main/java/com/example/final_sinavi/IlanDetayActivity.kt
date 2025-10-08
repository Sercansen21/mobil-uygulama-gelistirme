package com.example.final_sinavi

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class IlanDetayActivity : AppCompatActivity() {
    private lateinit var arabaResim: ImageView
    private lateinit var modelBaslik: TextView
    private lateinit var altBaslik: TextView
    private lateinit var fiyatDeger: TextView
    private lateinit var markaDeger: TextView
    private lateinit var saticiDeger: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ilan_detay)


        supportActionBar?.hide()


        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }


        arabaResim = findViewById(R.id.arabaResim)
        modelBaslik = findViewById(R.id.modelBaslik)
        altBaslik = findViewById(R.id.altBaslik)
        fiyatDeger = findViewById(R.id.fiyatDeger)
        markaDeger = findViewById(R.id.markaDeger)
        saticiDeger = findViewById(R.id.saticiDeger)


        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }


        val araba = intent.getParcelableExtra<Araba>("araba")


        araba?.let {
            modelBaslik.text = it.baslik
            altBaslik.text = "Hoş geldiniz, ${it.marka} ${it.model}"
            fiyatDeger.text = "${it.fiyat} TL"
            markaDeger.text = "${it.marka} ${it.model}"
            saticiDeger.text = it.kullaniciEmail ?: "Bilinmiyor"


            Glide.with(this)
                .load(it.resimUrl)
                .centerCrop()
                .into(arabaResim)
        }
    }
}