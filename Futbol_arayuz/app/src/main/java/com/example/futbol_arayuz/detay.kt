package com.example.futbol_arayuz

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class detay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detay)

        // View'ları tanımla
        val resimgoster = findViewById<ImageView>(R.id.imageView2)
        val txtIsim = findViewById<TextView>(R.id.txtIsim)
        val txtNumara = findViewById<TextView>(R.id.txtNumara)
        val txtPozisyon = findViewById<TextView>(R.id.txtPozisyon)
        val txtYas = findViewById<TextView>(R.id.txtYas)
        val txtTakim = findViewById<TextView>(R.id.txtTakim)
        val txtIstatistik = findViewById<TextView>(R.id.txtIstatistik)

        // Intent'ten gelen verileri al
        intent?.let {
            val resim = it.getIntExtra("resim", 0)
            val isim = it.getStringExtra("isim") ?: ""
            val numara = it.getIntExtra("forma_numarasi", 0)
            val pozisyon = it.getStringExtra("pozisyon") ?: ""
            val yas = it.getIntExtra("yas", 0)
            val takim = it.getStringExtra("takim") ?: ""
            val mac = it.getIntExtra("oynadigi_oyun", 0)
            val gol = it.getIntExtra("gol_sayisi", 0)
            val asist = it.getIntExtra("asist_sayisi", 0)

            // Verileri göster
            resimgoster.setImageResource(resim)
            txtIsim.text = isim
            txtNumara.text = "Forma Numarası: $numara"
            txtPozisyon.text = "Pozisyon: $pozisyon"
            txtYas.text = "Yaş: $yas"
            txtTakim.text = "Takım: $takim"
            txtIstatistik.text = """
                Maç Sayısı: $mac
                Gol Sayısı: $gol
                Asist Sayısı: $asist
            """.trimIndent()
        }
    }
}