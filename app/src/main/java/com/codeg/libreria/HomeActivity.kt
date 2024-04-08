package com.codeg.libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnAddBook.setOnClickListener{
            val gotoNextScreen = Intent(applicationContext, AddBookActivity::class.java)
            startActivity(gotoNextScreen)
        }

        btnAddUser.setOnClickListener{
            val gotoNextScreen = Intent(applicationContext, AddUserActivity::class.java)
            startActivity(gotoNextScreen)
        }
    }

}