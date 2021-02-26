package com.example.wac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SaMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sa_menu)

        val db = Firebase.firestore
        val logout = findViewById<Button>(R.id.logout_button)
        val username = findViewById<TextView>(R.id.username_sa)
        val addMenu = findViewById<ImageView>(R.id.add_menu)
        val navMove = findViewById<ImageView>(R.id.nav_move)


        val user = FirebaseAuth.getInstance().currentUser!!.email
            .toString().replace("@aka.com"," !")
        username.text = "HAI " + user.toUpperCase()

        logout.setOnClickListener {

            db.collection("pengguna").document(FirebaseAuth.getInstance().currentUser!!.email.toString())
                .delete()
                .addOnSuccessListener {
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(baseContext, "Logout Berhasil", Toast.LENGTH_SHORT).show()
                    val kembali = Intent(baseContext, Login::class.java);
                    startActivity(kembali)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, "Logout Gagal, Periksa Koneksi", Toast.LENGTH_SHORT).show()
                }

        }

        addMenu.setOnClickListener {
            val intent = Intent(baseContext, SaAdd::class.java)
            startActivity(intent)
        }

        navMove.setOnClickListener {
            val intent = Intent(baseContext, SaNav::class.java)
            startActivity(intent)
        }


    }
}