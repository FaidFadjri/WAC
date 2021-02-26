package com.example.wac

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class SaAdd : AppCompatActivity(){

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sa_add)

        val pickTanggal = findViewById<Button>(R.id.pick_tanggal)
        val displayDate = findViewById<TextView>(R.id.display_tanggal)
        val tipeKendaraan = findViewById<EditText>(R.id.tipe_car_column)
        val nomorKendaraan = findViewById<EditText>(R.id.nopol_car_column)
        val addButton = findViewById<Button>(R.id.add_button)
        val spinnerKendaraan = findViewById<Spinner>(R.id.spinner_kendaraan)
        val progress = findViewById<ProgressBar>(R.id.progress_add)

        pickTanggal.setOnClickListener {
            val cal = Calendar.getInstance()
            val y = cal.get(Calendar.YEAR)
            val m = cal.get(Calendar.MONTH)
            val d = cal.get(Calendar.DAY_OF_MONTH)


            val datepickerdialog:DatePickerDialog = DatePickerDialog(
                this@SaAdd,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    val MonthData = monthOfYear + 1
                    // Display Date
                    displayDate.visibility = View.VISIBLE
                    displayDate.text = "$dayOfMonth-$MonthData-$year"
                },
                y,
                m,
                d
            )

            datepickerdialog.show()
        }

        spinnerKendaraan?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (spinnerKendaraan.selectedItem.toString().equals("Lainnya")) {
                    tipeKendaraan.visibility = View.VISIBLE

                    addButton.setOnClickListener {
                        if (displayDate.text.toString().equals("Tanggal")) {
                            Toast.makeText(baseContext, "Harap Pilih Tanggal Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                        } else if (tipeKendaraan.text.toString().equals("Tanggal")) {
                            Toast.makeText(baseContext, "Harap Isi Tipe Kendaraan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                        } else if (nomorKendaraan.text.toString().equals("Tanggal")) {
                            Toast.makeText(baseContext, "Harap Isi Nomor Kendaraan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                        } else {

                            progress.visibility = View.VISIBLE
                            val dataMobil = hashMapOf(
                                "tanggal" to displayDate.text.toString(),
                                "nopol" to nomorKendaraan.text.toString(),
                                "tipe" to tipeKendaraan.text.toString()
                            )

                            db.collection("mobil").document(nomorKendaraan.text.toString()).set(dataMobil)
                                .addOnSuccessListener {
                                    Toast.makeText(baseContext, "Mobil Berhasil Di Tambahkan", Toast.LENGTH_SHORT).show()
                                    val search_keyword = generateSearchKeyword(nomorKendaraan.text.toString())
                                    val data = HashMap<String, Any>()
                                    data["search_keyword"] = search_keyword
                                    db.collection("mobil").document(nomorKendaraan.text.toString()).set(data, SetOptions.merge())
                                        .addOnSuccessListener {
                                            nomorKendaraan.setText("")
                                            tipeKendaraan.setText("")
                                            spinnerKendaraan.setSelection(0)
                                            progress.visibility = View.GONE
                                        }
                                        .addOnFailureListener {
                                            nomorKendaraan.setText("")
                                            tipeKendaraan.setText("")
                                            spinnerKendaraan.setSelection(0)
                                            progress.visibility = View.GONE
                                        }


                                }
                                .addOnFailureListener {
                                    Toast.makeText(baseContext, "Gagal Menambahkan Mobil, Periksa Koneksi", Toast.LENGTH_SHORT).show()
                                    progress.visibility = View.GONE
                                }
                        }
                    }

                } else {
                    tipeKendaraan.visibility = View.GONE
                    addButton.setOnClickListener {
                        if (displayDate.text.toString().equals("Tanggal")) {
                            Toast.makeText(baseContext, "Harap Pilih Tanggal Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                        } else if (nomorKendaraan.text.toString().equals("Tanggal")) {
                            Toast.makeText(baseContext, "Harap Isi Nomor Kendaraan Terlebih Dahulu", Toast.LENGTH_SHORT).show()
                        } else {
                            progress.visibility = View.VISIBLE
                            val dataMobil = hashMapOf(
                                "tanggal" to displayDate.text.toString(),
                                "nopol" to nomorKendaraan.text.toString(),
                                "tipe" to spinnerKendaraan.selectedItem.toString()
                            )

                            db.collection("mobil").document(nomorKendaraan.text.toString()).set(dataMobil)
                                .addOnSuccessListener {
                                    Toast.makeText(baseContext, "Mobil Berhasil Di Tambahkan", Toast.LENGTH_SHORT).show()
                                    val search_keyword = generateSearchKeyword(nomorKendaraan.text.toString())
                                    val data = HashMap<String, Any>()
                                    data["search_keyword"] = search_keyword
                                    db.collection("mobil").document(nomorKendaraan.text.toString()).set(data, SetOptions.merge())
                                        .addOnSuccessListener {
                                            nomorKendaraan.setText("")
                                            tipeKendaraan.setText("")
                                            spinnerKendaraan.setSelection(0)
                                            progress.visibility = View.GONE
                                        }
                                        .addOnFailureListener {
                                            nomorKendaraan.setText("")
                                            tipeKendaraan.setText("")
                                            spinnerKendaraan.setSelection(0)
                                            progress.visibility = View.GONE
                                        }
                                }
                                .addOnFailureListener {
                                    Toast.makeText(baseContext, "Gagal Menambahkan Mobil, Periksa Koneksi", Toast.LENGTH_SHORT).show()
                                    progress.visibility = View.GONE
                                }
                        }
                    }
                }
            }

        }
    }

    private fun generateSearchKeyword(notiftitle: String): List<String> {

        val plat = findViewById<EditText>(R.id.nopol_car_column)

        var inputString = plat.text.toString().toUpperCase()
        var keywords = mutableListOf<String>()

//        split
        val words = inputString.split("(?!^)")

//        for each word
        for (word in words){
            var appendString = ""

//            every character
            for (charPosition in inputString.indices) {
                appendString += inputString[charPosition].toString()
                keywords.add(appendString)
            }
            inputString.replace("$word ","").removePrefix(" ")
        }

        return keywords
    }

}