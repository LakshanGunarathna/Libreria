package com.codeg.libreria

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddLendingActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTxtUserID: EditText
    private lateinit var editTxtBook1: EditText
    private lateinit var editTxtBook2: EditText
    private lateinit var btnScanUID :Button
    private lateinit var btnScanBook1 :Button
    private lateinit var btnScanBook2 :Button
    private lateinit var db: LibreriaDB


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_lending)

        editTxtUserID = findViewById(R.id.editTxtUserId)
        editTxtBook1 = findViewById(R.id.editTxtISBNBook1)
        editTxtBook2 = findViewById(R.id.editTxtISBNBook2)

        btnScanUID = findViewById(R.id.btnScanUID)
        btnScanBook1 = findViewById(R.id.btnScanBook1)
        btnScanBook2 = findViewById(R.id.btnScanBook2)

        // Create an instance of CodeScanner with the application context
        codeScanner = CodeScanner(this)

        db = LibreriaDB(this)

        val saveButton = findViewById<Button>(R.id.btnAddLending)
        saveButton.setOnClickListener {
            saveLending()
        }

        val cancelButton: Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous screen
        }

        btnScanUID.setOnClickListener{
            onScanButtonClicked(editTxtUserID)
        }

        btnScanBook1.setOnClickListener{
            onScanButtonClicked(editTxtBook1)
        }

        btnScanBook2.setOnClickListener{
            onScanButtonClicked(editTxtBook2)
        }

    }

    fun onScanButtonClicked(inputField: EditText) {
        // Start scanning using the CodeScanner instance
        codeScanner.startScanning(
            callback = { result ->
                runOnUiThread {
                    // Update UI on the main thread
                    inputField.setText(result)
                    // Use setText instead of text if needed
                }
            },
            errorCallback = { error ->
                runOnUiThread {
                    // Update UI on the main thread
                    showToast(error)

                }
            }
        )

    }


    private fun saveLending() {
        val userID = editTxtUserID.text.toString()
        val book1 = editTxtBook1.text.toString()
        val book2 = editTxtBook2.text.toString()

        // Add book data to the database
        //db.insertBookData(isbn, title, author, genre, numOfCopies, "Available", this, bitmap)

        // Redirect to the MainActivity and open the BooksFragment
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("fragmentToOpen", "LendingFragment")
        }
        startActivity(intent)

        // Finish the current activity
        finish()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        codeScanner.onRequestPermissionsResult(requestCode, grantResults)
    }
}