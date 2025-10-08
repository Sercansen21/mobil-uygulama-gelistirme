package com.example.final_sinavi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.slider.RangeSlider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class AramaActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var arabaListesi: ArrayList<Araba>
    private lateinit var adapter: ArabaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: TextView
    private lateinit var searchEditText: EditText
    private lateinit var priceRangeSlider: RangeSlider
    private lateinit var priceFilterButton: ImageButton
    private lateinit var filterLayout: LinearLayout
    private var selectedLogo: ImageView? = null
    private var currentBrand: String? = null
    private var minPrice: Float = 0f
    private var maxPrice: Float = 10000000f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arama)

        initializeViews()
        setupRecyclerView()
        setupListeners()
        loadAllCars()
    }

    private fun initializeViews() {
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        searchEditText = findViewById(R.id.searchEditText)
        priceRangeSlider = findViewById(R.id.priceRangeSlider)
        priceFilterButton = findViewById(R.id.priceFilterButton)
        filterLayout = findViewById(R.id.filterLayout)

        db = FirebaseFirestore.getInstance()
        arabaListesi = ArrayList()

        // Fiyat slider'ı ayarları
        priceRangeSlider.valueFrom = 0f
        priceRangeSlider.valueTo = 10000000f
        priceRangeSlider.values = listOf(0f, 10000000f)

        // Filtre layout'u başlangıçta gizli
        filterLayout.visibility = View.GONE
    }

    private fun setupRecyclerView() {
        adapter = ArabaAdapter(arabaListesi)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AramaActivity)
            adapter = this@AramaActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupListeners() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        priceFilterButton.setOnClickListener {
            filterLayout.visibility = if (filterLayout.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        priceRangeSlider.addOnChangeListener { slider, _, _ ->
            val values = slider.values
            minPrice = values[0]
            maxPrice = values[1]
            applyFilters()
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                selectedLogo?.alpha = 1.0f
                selectedLogo = null
                currentBrand = null
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        setupLogoButtons()
    }

    private fun setupLogoButtons() {
        val logoButtons = mapOf(
            R.id.bmwLogo to "BMW",
            R.id.audiLogo to "Audi",
            R.id.mercedesLogo to "Mercedes",
            R.id.volkswagenLogo to "Volkswagen",
            R.id.kiaLogo to "Kia",
            R.id.toyotaLogo to "Toyota",
            R.id.fordLogo to "Ford",
            R.id.hondaLogo to "Honda",
            R.id.hyundaiLogo to "Hyundai"
        )

        logoButtons.forEach { (id, brand) ->
            findViewById<ImageView>(id).apply {
                setBackgroundResource(R.drawable.logo_background_ripple)
                setOnClickListener {
                    selectedLogo?.alpha = 1.0f
                    this.alpha = 0.5f
                    selectedLogo = this
                    searchEditText.setText("")
                    currentBrand = brand
                    applyFilters()
                }
            }
        }
    }

    private fun applyFilters() {
        db.collection("arabalar")
            .get()
            .addOnSuccessListener { documents ->
                arabaListesi.clear()
                for (document in documents) {
                    val araba = document.toObject(Araba::class.java)
                    var shouldAdd = true

                    // Fiyat kontrolü
                    val fiyat = araba.fiyat.replace(".", "").toFloatOrNull() ?: 0f
                    if (fiyat < minPrice || fiyat > maxPrice) {
                        shouldAdd = false
                    }

                    // Marka kontrolü
                    if (currentBrand != null && araba.marka != currentBrand) {
                        shouldAdd = false
                    }

                    // Arama metni kontrolü
                    val searchText = searchEditText.text.toString().toLowerCase()
                    if (searchText.isNotEmpty()) {
                        if (!araba.baslik.toLowerCase().contains(searchText) &&
                            !araba.marka.toLowerCase().contains(searchText) &&
                            !araba.model.toLowerCase().contains(searchText)) {
                            shouldAdd = false
                        }
                    }

                    if (shouldAdd) {
                        arabaListesi.add(araba)
                    }
                }

                // Tarihe göre sırala
                arabaListesi.sortByDescending { it.tarih }
                adapter.notifyDataSetChanged()
                updateEmptyView()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Filtreleme hatası: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadAllCars() {
        db.collection("arabalar")
            .orderBy("tarih", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Toast.makeText(this, "Veri yükleme hatası", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                arabaListesi.clear()
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val araba = document.toObject(Araba::class.java)
                        araba?.let { arabaListesi.add(it) }
                    }
                }
                adapter.notifyDataSetChanged()
                updateEmptyView()
            }
    }

    private fun updateEmptyView() {
        if (arabaListesi.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }
    }
}