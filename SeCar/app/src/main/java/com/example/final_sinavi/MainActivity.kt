package com.example.final_sinavi

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArabaAdapter
    private val arabaListesi = mutableListOf<Araba>()

    private lateinit var emailGiris: TextInputEditText
    private lateinit var sifreGiris: TextInputEditText
    private lateinit var girisButon: MaterialButton
    private lateinit var kayitButon: MaterialButton
    private lateinit var loginLayout: LinearLayout
    private lateinit var emptyView: TextView
    private lateinit var mainContent: View

    private val ILAN_EKLE_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        viewBilesenleriBaslat()
        recyclerViewKurulum()
        butonKurulum()
        arabalariYukle()
        kullaniciDurumuKontrol()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val addCarItem = menu?.findItem(R.id.action_add_car)
        val logoutItem = menu?.findItem(R.id.action_logout)
        val searchItem = menu?.findItem(R.id.action_search)

        val userLoggedIn = auth.currentUser != null
        addCarItem?.isVisible = userLoggedIn
        logoutItem?.isVisible = userLoggedIn
        searchItem?.isVisible = true

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                startActivity(Intent(this, AramaActivity::class.java))
                true
            }
            R.id.action_add_car -> {
                if (auth.currentUser != null) {
                    startActivityForResult(Intent(this, IlanEkleActivity::class.java), ILAN_EKLE_REQUEST)
                } else {
                    Toast.makeText(this, "Lütfen önce giriş yapın", Toast.LENGTH_SHORT).show()
                }
                true
            }
            R.id.action_logout -> {
                auth.signOut()
                kullaniciDurumuKontrol()
                Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ILAN_EKLE_REQUEST && resultCode == RESULT_OK) {
            arabalariYukle()
        }
    }

    private fun viewBilesenleriBaslat() {
        // Login bileşenleri
        loginLayout = findViewById(R.id.loginCard)
        emailGiris = findViewById(R.id.emailGiris)
        sifreGiris = findViewById(R.id.sifreGiris)
        girisButon = findViewById(R.id.girisButon)
        kayitButon = findViewById(R.id.kayitButon)

        // Ana içerik bileşenleri
        mainContent = findViewById(R.id.mainContent)
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
    }

    private fun recyclerViewKurulum() {
        adapter = ArabaAdapter(arabaListesi)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun butonKurulum() {
        girisButon.setOnClickListener {
            val email = emailGiris.text.toString()
            val sifre = sifreGiris.text.toString()

            if (email.isNotEmpty() && sifre.isNotEmpty()) {
                girisYap(email, sifre)
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }

        kayitButon.setOnClickListener {
            val email = emailGiris.text.toString()
            val sifre = sifreGiris.text.toString()

            if (email.isNotEmpty() && sifre.isNotEmpty()) {
                kayitOl(email, sifre)
            } else {
                Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun girisYap(email: String, sifre: String) {
        auth.signInWithEmailAndPassword(email, sifre)
            .addOnSuccessListener {
                Toast.makeText(this, "Giriş başarılı", Toast.LENGTH_SHORT).show()
                checkImagePermission()
                kullaniciDurumuKontrol()
                invalidateOptionsMenu()
                emailGiris.text?.clear()
                sifreGiris.text?.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Giriş hatası: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun kayitOl(email: String, sifre: String) {
        auth.createUserWithEmailAndPassword(email, sifre)
            .addOnSuccessListener {
                Toast.makeText(this, "Kayıt başarılı", Toast.LENGTH_SHORT).show()
                checkImagePermission()
                kullaniciDurumuKontrol()
                invalidateOptionsMenu()
                emailGiris.text?.clear()
                sifreGiris.text?.clear()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Kayıt hatası: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkImagePermission() {
        val izin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(this, izin) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(izin), 1001)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Resim izni verildi", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Resim izni reddedildi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun kullaniciDurumuKontrol() {
        if (auth.currentUser != null) {
            loginLayout.visibility = View.GONE
            mainContent.visibility = View.VISIBLE
            arabalariYukle()
        } else {
            loginLayout.visibility = View.VISIBLE
            mainContent.visibility = View.GONE
            arabaListesi.clear()
            adapter.notifyDataSetChanged()
        }
        invalidateOptionsMenu()
    }


    private fun arabalariYukle() {
        try {
            db.collection("arabalar")
                .orderBy("tarih", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.e("FirestoreHata", "Veri yükleme hatası", e)
                        return@addSnapshotListener
                    }

                    arabaListesi.clear()
                    if (snapshot != null) {
                        for (document in snapshot.documents) {
                            val araba = document.toObject(Araba::class.java)
                            araba?.let { arabaListesi.add(it) }
                        }
                        adapter.notifyDataSetChanged()

                        if (arabaListesi.isEmpty()) {
                            recyclerView.visibility = View.GONE
                            emptyView.visibility = View.VISIBLE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            emptyView.visibility = View.GONE
                        }
                    }
                }
        } catch (e: Exception) {
            Log.e("FirestoreHata", "Beklenmeyen hata", e)
            Toast.makeText(this, "Beklenmeyen bir hata oluştu", Toast.LENGTH_SHORT).show()
        }
    }
}