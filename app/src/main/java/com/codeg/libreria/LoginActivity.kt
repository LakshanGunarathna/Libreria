package com.codeg.libreria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun LoginCheck(view: View) {

        val username: EditText = findViewById(R.id.editTxtUserName)
        val password: EditText = findViewById(R.id.editTxtPassword)

        if (username.text.toString() == "lk" &&
            password.text.toString() == "1") {
            val gotoNextScreen = Intent(applicationContext,AddBookActivity::class.java)
            startActivity(gotoNextScreen)
        } else {
            showToast("Login Error!!!")
        }
    }

    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}