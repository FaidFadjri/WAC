package com.example.wac

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class CheckMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_menu)

        val menuBattery = findViewById<ConstraintLayout>(R.id.menu_battery)

        menuBattery.setOnClickListener {
            val intent = Intent(baseContext, CheckBattery::class.java)
            startActivity(intent)
        }

    }
}