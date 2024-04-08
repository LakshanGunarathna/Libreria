package com.codeg.libreria

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    val gotoNextScreen = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(gotoNextScreen)
                    true
                }
                R.id.navigation_book -> {
                    val gotoNextScreen = Intent(applicationContext, AddBookActivity::class.java)
                    startActivity(gotoNextScreen)
                    true
                }
                R.id.navigation_user -> {
                    val gotoNextScreen = Intent(applicationContext, AddUserActivity::class.java)
                    startActivity(gotoNextScreen)
                    true
                }
                R.id.navigation_lending -> {
                    val gotoNextScreen = Intent(applicationContext, AddUserActivity::class.java)
                    startActivity(gotoNextScreen)
                    true
                }
                R.id.navigation_admin -> {
                    val gotoNextScreen = Intent(applicationContext, AdminActivity::class.java)
                    startActivity(gotoNextScreen)
                    true
                }
                else -> false
            }
        }
    }

}

