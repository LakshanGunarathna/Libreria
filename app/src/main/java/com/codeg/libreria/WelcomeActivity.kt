package com.codeg.libreria

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.codeg.libreria.activity.LoginActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        if (isFirstTime()) {
            // Show welcome activity
            btnGetStart.setOnClickListener {
                val editor = sharedPreferences.edit()
                editor.putBoolean("isFirstTime", false)
                editor.apply()

                val gotoNextScreen = Intent(applicationContext, LoginActivity::class.java)
                startActivity(gotoNextScreen)
                finish()
            }
        } else {
            // App has already been opened before, go directly to login activity
            val gotoNextScreen = Intent(applicationContext, LoginActivity::class.java)
            startActivity(gotoNextScreen)
            finish()
        }
    }

    private fun isFirstTime(): Boolean {
        return sharedPreferences.getBoolean("isFirstTime", true)
    }
}
