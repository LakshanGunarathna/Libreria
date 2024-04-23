package com.codeg.libreria

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream

class AddAdminActivity : AppCompatActivity() {

    private lateinit var db: LibreriaDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_admin)

        db = LibreriaDB(this)

        val nameInput: EditText = findViewById(R.id.editTxtName)
        val addressInput: EditText = findViewById(R.id.editTxtAddress)
        val telNoInput: EditText = findViewById(R.id.editTxtTelNo)
        val emailInput: EditText = findViewById(R.id.editTxtEmail)
        val usernameInput: EditText = findViewById(R.id.editTxtUserName)
        val passwordInput: EditText = findViewById(R.id.editTxtPassword)
        val addButton: Button = findViewById(R.id.btnAddAdmin)

        addButton.setOnClickListener {
            val name = nameInput.text.toString()
            val address = addressInput.text.toString()
            val telNo = telNoInput.text.toString()
            val email = emailInput.text.toString()
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            // Insert admin data into the database
            db.insertAdminData(username, password, name, address, telNo, email)

            // Show a toast message upon successful insertion
            Toast.makeText(this, "Admin added successfully", Toast.LENGTH_SHORT).show()

            // Clear input fields after adding the admin
            nameInput.text.clear()
            addressInput.text.clear()
            telNoInput.text.clear()
            emailInput.text.clear()
            usernameInput.text.clear()
            passwordInput.text.clear()

            // Redirect to the MainActivity and open the BooksFragment
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("fragmentToOpen", "AdminFragment")
            }
            startActivity(intent)
        }

        val cancelButton: Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous screen
        }
    }
}
