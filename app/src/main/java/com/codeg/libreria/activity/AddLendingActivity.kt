package com.codeg.libreria.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.codeg.libreria.assest.CodeScanner
import com.codeg.libreria.assest.LibreriaDB
import com.codeg.libreria.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddLendingActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTxtUserID: EditText
    private lateinit var editTxtBook1: EditText
    private lateinit var editTxtBook2: EditText
    private lateinit var btnScanUID: ImageButton
    private lateinit var btnScanBook1: ImageButton
    private lateinit var btnScanBook2: ImageButton
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

        // Check if user ID and book ISBNs exist in the database
        if (!isUserExists(userID)) {
            showToast("User ID not found")
            return
        }

        if (!isBookExists(book1)) {
            showToast("Book 1 ISBN not found")
            return
        }

        if (!book2.isEmpty() && !isBookExists(book2)) {
            showToast("Book 2 ISBN not found")
            return
        }

        // Assuming borrowing date is the current date
        val borrowingDate = getCurrentDateTime()

        // Assuming due date is 14 days from the borrowing date
        val dueDate = getDueDate(borrowingDate)

        // Insert lending data into the database
        db.insertBorrowingData(userID, book1, book2, borrowingDate, dueDate, null)

        // Redirect to the MainActivity and open the LendingFragment
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("fragmentToOpen", "LendingFragment")
        }
        startActivity(intent)

        // Finish the current activity
        finish()
    }

    private fun isUserExists(userID: String): Boolean {
        // Query the database to check if the user ID exists
        return db.getAllUsers().any { it.userId == userID }
    }

    private fun isBookExists(isbn: String): Boolean {
        // Query the database to check if the book ISBN exists
        return db.getAllBooks().any { it.isbn == isbn }
    }


    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun getDueDate(borrowingDate: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = formatter.parse(borrowingDate) ?: Date()
        calendar.add(Calendar.DATE, 14) // Add 14 days to borrowing date
        return formatter.format(calendar.time)
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