package com.example.final_sinavi

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class IlanEkleActivity : AppCompatActivity() {
    private lateinit var resimSecButon: MaterialButton
    private lateinit var secilenResimView: ImageView
    private lateinit var baslikEdit: TextInputEditText
    private lateinit var markaAutoComplete: AutoCompleteTextView
    private lateinit var modelAutoComplete: AutoCompleteTextView
    private lateinit var fiyatEdit: TextInputEditText
    private lateinit var iptalButon: MaterialButton
    private lateinit var ilanPaylas: MaterialButton

    private var secilenResimUri: Uri? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private val markalar = mapOf(
        "BMW" to listOf("1 Serisi", "2 Serisi", "3 Serisi", "4 Serisi", "5 Serisi", "6 Serisi", "7 Serisi", "X1", "X3", "X5", "X6", "M3", "M4", "M5"),
        "Mercedes" to listOf("A Serisi", "B Serisi", "C Serisi", "E Serisi", "S Serisi", "CLA", "CLS", "GLA", "GLC", "GLE", "AMG GT"),
        "Audi" to listOf("A1", "A3", "A4", "A5", "A6", "A7", "A8", "Q2", "Q3", "Q5", "Q7", "Q8", "RS3", "RS4", "RS6"),
        "Volkswagen" to listOf("Polo", "Golf", "Passat", "Arteon", "T-Roc", "Tiguan", "Touareg", "ID.3", "ID.4"),
        "Toyota" to listOf("Corolla", "Yaris", "C-HR", "RAV4", "Camry", "Land Cruiser", "Supra"),
        "Honda" to listOf("Civic", "City", "CR-V", "HR-V", "Jazz"),
        "Hyundai" to listOf("i10", "i20", "i30", "Tucson", "Santa Fe", "KONA", "IONIQ"),
        "Kia" to listOf("Picanto", "Rio", "Ceed", "Sportage", "Sorento", "EV6"),
        "Ford" to listOf("Fiesta", "Focus", "Mondeo", "Kuga", "Puma", "Mustang")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ilan_ekle)

        storage = FirebaseStorage.getInstance()
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        viewBaglama()
        markaModelAyarla()
        butonTiklamaOlaylari()
    }

    private fun viewBaglama() {
        resimSecButon = findViewById(R.id.resimSecButon)
        secilenResimView = findViewById(R.id.secilenResimView)
        baslikEdit = findViewById(R.id.baslikEdit)
        markaAutoComplete = findViewById(R.id.markaAutoComplete)
        modelAutoComplete = findViewById(R.id.modelAutoComplete)
        fiyatEdit = findViewById(R.id.fiyatEdit)
        iptalButon = findViewById(R.id.iptalButon)
        ilanPaylas = findViewById(R.id.ilanPaylas)
    }

    private fun markaModelAyarla() {
        val markaAdapter = MarkaAdapter(this)
        markaAutoComplete.setAdapter(markaAdapter)
        markaAutoComplete.threshold = 1

        markaAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val secilenMarka = markaAdapter.getItem(position)
            markaAutoComplete.setText(secilenMarka.ad)

            val modeller = markalar[secilenMarka.ad] ?: emptyList()
            val modelAdapter = ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line,
                modeller)
            modelAutoComplete.setAdapter(modelAdapter)
            modelAutoComplete.text.clear()
            modelAutoComplete.threshold = 1
        }

        markaAutoComplete.setDropDownBackgroundResource(android.R.color.white)
        markaAutoComplete.dropDownVerticalOffset = 20
    }

    private fun butonTiklamaOlaylari() {
        resimSecButon.setOnClickListener {
            resimSec()
        }

        iptalButon.setOnClickListener {
            finish()
        }

        ilanPaylas.setOnClickListener {
            ilanPaylas()
        }
    }

    private fun resimSec() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            secilenResimUri = data?.data
            secilenResimUri?.let {
                secilenResimView.setImageURI(it)
                secilenResimView.visibility = View.VISIBLE
            }
        }
    }

    private fun ilanPaylas() {
        val baslik = baslikEdit.text.toString()
        val marka = markaAutoComplete.text.toString()
        val model = modelAutoComplete.text.toString()
        val fiyat = fiyatEdit.text.toString()

        if (secilenResimUri == null || baslik.isEmpty() || marka.isEmpty() ||
            model.isEmpty() || fiyat.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("İlan paylaşılıyor...")
        progressDialog.show()

        val resimRef = storage.reference.child("ilanlar/${System.currentTimeMillis()}.jpg")
        resimRef.putFile(secilenResimUri!!)
            .addOnSuccessListener { taskSnapshot ->
                resimRef.downloadUrl.addOnSuccessListener { uri ->
                    val ilan = hashMapOf(
                        "baslik" to baslik,
                        "marka" to marka,
                        "model" to model,
                        "fiyat" to fiyat,
                        "resimUrl" to uri.toString(),
                        "kullaniciEmail" to auth.currentUser?.email,
                        "tarih" to System.currentTimeMillis()
                    )

                    db.collection("arabalar")
                        .add(ilan)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "İlan başarıyla paylaşıldı", Toast.LENGTH_SHORT).show()
                            setResult(RESULT_OK)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(this, "Hata: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Resim yükleme hatası: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}