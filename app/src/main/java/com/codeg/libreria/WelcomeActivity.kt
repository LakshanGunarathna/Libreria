package com.codeg.libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        btnGetStart.setOnClickListener{
            val gotoNextScreen = Intent(applicationContext,LoginActivity::class.java)
            startActivity(gotoNextScreen)

        }
    }
}