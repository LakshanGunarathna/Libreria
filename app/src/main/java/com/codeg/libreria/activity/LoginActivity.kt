package com.codeg.libreria.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeg.libreria.assest.LibreriaDB
import com.codeg.libreria.R

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var db: LibreriaDB

    private val logoutDelayMillis: Long = 3600000 // 1 hour in milliseconds
    private val handler = Handler()
    private val logoutRunnable = Runnable { logoutUser() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = LibreriaDB(this)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        username = findViewById(R.id.editTxtUserName)
        password = findViewById(R.id.editTxtPassword)

        // Check if there are any admins in the database
        if (!db.adminsExist()) {
            // No admins exist, navigate the user to create the admin account
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish() // Finish the LoginActivity to prevent going back
            return // Return to prevent further execution
        }

        checkLoginStatus()

        val cancelButton: Button = findViewById(R.id.btnLoginCancel)
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun checkLoginStatus() {
        val lastLoginTime = sharedPreferences.getLong("lastLoginTime", 0)
        if (System.currentTimeMillis() - lastLoginTime < logoutDelayMillis) {
            // User is still logged in
            navigateToMainActivity()
        } else {
            // User has been inactive for too long, automatically logout
            logoutUser()
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun loginSuccess(adminId: Int) {
        sharedPreferences.edit().putInt("currentAdminId", adminId).apply()
        sharedPreferences.edit().putLong("lastLoginTime", System.currentTimeMillis()).apply()
        navigateToMainActivity()
    }

    private fun logoutUser() {
        sharedPreferences.edit().remove("currentAdminId").apply()
        sharedPreferences.edit().remove("lastLoginTime").apply()
        showToast("Logged out from Libreria")
        // Perform any other logout tasks here if needed
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(logoutRunnable, logoutDelayMillis)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(logoutRunnable)
    }

    fun LoginCheck(view: View) {
        val enteredUsername = username.text.toString()
        val enteredPassword = password.text.toString()

        // Check if the entered credentials match any admin in the database
        val admin = db.getAdminByUsernameAndPassword(enteredUsername, enteredPassword)
        if (admin != null) {
            loginSuccess(admin.id) // Pass the admin ID to loginSuccess
        } else {
            showToast("Invalid username or password")
        }
    }
}
