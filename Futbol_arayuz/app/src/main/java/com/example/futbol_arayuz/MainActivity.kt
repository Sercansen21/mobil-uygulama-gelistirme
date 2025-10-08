package com.example.futbol_arayuz

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.futbol_arayuz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val volkanDemirel = Player("Volkan Demirel", 26, "Kaleci", "Fenerbahçe", 1, 34, 0, 0, R.drawable.img)
        val eduDracena = Player("Edu Dracena", 26, "Defans", "Fenerbahçe", 4, 28, 2, 1, R.drawable.img_2)
        val lugano = Player("Diego Lugano", 27, "Defans", "Fenerbahçe", 2, 30, 3, 1, R.drawable.img_1)
        val robertoCarlos = Player("Roberto Carlos", 34, "Defans", "Fenerbahçe", 3, 26, 2, 6, R.drawable.img_3)
        val gokhanGonul = Player("Gökhan Gönül", 23, "Defans", "Fenerbahçe", 77, 22, 1, 3, R.drawable.img_4)
        val emreBelozoglu = Player("Emre Belözoğlu", 27, "Orta Saha", "Fenerbahçe", 5, 28, 4, 8, R.drawable.img_5)
        val alex = Player("Alex de Souza", 30, "Orta Saha", "Fenerbahçe", 10, 32, 15, 12, R.drawable.img_6)
        val ugurBoral = Player("Uğur Boral", 25, "Orta Saha", "Fenerbahçe", 21, 25, 3, 5, R.drawable.img_7)
        val deivid = Player("Deivid", 28, "Forvet", "Fenerbahçe", 99, 30, 12, 6, R.drawable.img_8)
        val semihSenturk = Player("Semih Şentürk", 24, "Forvet", "Fenerbahçe", 9, 29, 11, 4, R.drawable.img_10)
        val colinKazim = Player("Colin Kazım Richards", 21, "Forvet", "Fenerbahçe", 7, 24, 5, 7, R.drawable.img_9)
        val fb2008Kadro = ArrayList<Player>()
        fb2008Kadro.add(volkanDemirel)
        fb2008Kadro.add(eduDracena)
        fb2008Kadro.add(lugano)
        fb2008Kadro.add(robertoCarlos)
        fb2008Kadro.add(gokhanGonul)
        fb2008Kadro.add(emreBelozoglu)
        fb2008Kadro.add(alex)
        fb2008Kadro.add(ugurBoral)
        fb2008Kadro.add(deivid)
        fb2008Kadro.add(semihSenturk)
        fb2008Kadro.add(colinKazim)
        val adapter = Myadapter(fb2008Kadro)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager=LinearLayoutManager(this)
    }

}