package com.example.wac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //Initialize Component
        val username_column = findViewById<EditText>(R.id.username_column)
        val password_column = findViewById<EditText>(R.id.password_column)
        val user_role = findViewById<Spinner>(R.id.role_user)
        val login_button = findViewById<Button>(R.id.login_button)


        // Initialize Firebase Auth
        auth = Firebase.auth

        login_button.setOnClickListener {

            //get Value from login form
            val email = username_column.text.toString()
            val password = password_column.text.toString()
            val role = user_role.selectedItem.toString()

            if (email.equals("") || password.equals("")) {
                Toast.makeText(baseContext,"Email Atau Password Kosong", Toast.LENGTH_SHORT).show()
            } else {


                //Code For Login
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            val userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()

                            if (role.equals("SA")) {
                                val intent = Intent(baseContext, SaMenu::class.java)
                                startActivity(intent)
                                finish()
                            }
                            Toast.makeText(baseContext, "Login Berhasil", Toast.LENGTH_SHORT).show()

                            val userData = hashMapOf(
                                "email" to userEmail,
                                "role" to role
                            )

                            db.collection("pengguna").document(userEmail).set(userData);

                        } else {
                            Toast.makeText(baseContext,"Login Gagal", Toast.LENGTH_SHORT).show()
                        }

                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            val userEmail = FirebaseAuth.getInstance().currentUser!!.email.toString()
            db.collection("pengguna").document(userEmail).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val roleData = document.getString("role")
                        if (roleData.equals("SA")) {
                            val intent = Intent(baseContext, SaMenu::class.java)
                            startActivity(intent)
                            finish()
                        } else if (roleData.equals("MRA")) {
                            Toast.makeText(baseContext,"as MRA", Toast.LENGTH_SHORT).show()
                        } else if (roleData.equals("Teknisi")) {
                            Toast.makeText(baseContext,"as Teknisi", Toast.LENGTH_SHORT).show()
                        }
                    } else {

                    }
                }
        }
    }
}