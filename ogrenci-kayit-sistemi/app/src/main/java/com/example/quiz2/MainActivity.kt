package com.example.quiz2

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var adapter: ArrayAdapter<String>
    private val studentList = ArrayList<String>()
    private val studentIds = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = AppDatabase.getDatabase(this)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, studentList)
        val listView = findViewById<ListView>(R.id.listViewStudents)
        listView.adapter = adapter

        // ListView'a uzun basma olayını ekleme
        listView.setOnItemLongClickListener { _, _, position, _ ->
            showDeleteDialog(position)
            true
        }

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            saveStudent()
        }

        updateStudentList()
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Öğrenci Sil")
            .setMessage("Bu öğrenciyi silmek istediğinizden emin misiniz?")
            .setPositiveButton("Evet") { _, _ ->
                deleteStudent(position)
            }
            .setNegativeButton("Hayır", null)
            .show()
    }

    private fun deleteStudent(position: Int) {
        val studentId = studentIds[position]
        CoroutineScope(Dispatchers.IO).launch {
            db.studentDao().deleteStudent(studentId)
            withContext(Dispatchers.Main) {
                updateStudentList()
                Toast.makeText(this@MainActivity, "Öğrenci silindi", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateStudentList() {
        CoroutineScope(Dispatchers.IO).launch {
            val students = db.studentDao().getAllStudents()

            withContext(Dispatchers.Main) {
                studentList.clear()
                studentIds.clear()

                students.forEach { student ->
                    studentIds.add(student.id)
                    studentList.add(
                        "Ad: ${student.firstName}\n" +
                                "Soyad: ${student.lastName}\n" +
                                "Öğrenci No: ${student.number}\n" +
                                "Alttan Ders: ${if (student.hasCourse) "Var" else "Yok"}"
                    )
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun saveStudent() {
        val firstName = findViewById<EditText>(R.id.editTextFirstName).text.toString()
        val lastName = findViewById<EditText>(R.id.editTextLastName).text.toString()
        val number = findViewById<EditText>(R.id.editTextStudentNumber).text.toString()
        val hasCourse = findViewById<CheckBox>(R.id.checkBoxCourse).isChecked

        if (firstName.isNotEmpty() && lastName.isNotEmpty() && number.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                db.studentDao().insertStudent(Student(
                    firstName = firstName,
                    lastName = lastName,
                    number = number,
                    hasCourse = hasCourse
                ))

                withContext(Dispatchers.Main) {
                    updateStudentList()
                    clearInputs()
                    Toast.makeText(this@MainActivity, "Öğrenci başarıyla kaydedildi", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputs() {
        findViewById<EditText>(R.id.editTextFirstName).text.clear()
        findViewById<EditText>(R.id.editTextLastName).text.clear()
        findViewById<EditText>(R.id.editTextStudentNumber).text.clear()
        findViewById<CheckBox>(R.id.checkBoxCourse).isChecked = false
    }
}