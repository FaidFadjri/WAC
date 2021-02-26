package com.example.wac

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class SaNav : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sa_nav)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val home = NavHome()
        val progress = NavProgress()
        val profile = NavProfile()

        makeCurrentFragment(home)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> makeCurrentFragment(home)
                R.id.nav_process -> makeCurrentFragment(progress)
                R.id.nav_profile -> makeCurrentFragment(profile)
            }
            true
        }

    }

    private fun makeCurrentFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }
    }


}