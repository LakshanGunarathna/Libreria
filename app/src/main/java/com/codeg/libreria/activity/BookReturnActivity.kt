package com.codeg.libreria.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codeg.libreria.assest.CodeScanner
import com.codeg.libreria.assest.LibreriaDB
import com.codeg.libreria.R
import java.text.SimpleDateFormat
import java.util.*

class BookReturnActivity : AppCompatActivity() {

    private lateinit var db: LibreriaDB

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTxtUserID: EditText
    private lateinit var editTxtBook1: EditText
    private lateinit var editTxtBook2: EditText
    private lateinit var btnScanUID: ImageButton
    private lateinit var btnScanBook1: ImageButton
    private lateinit var btnScanBook2: ImageButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_return)

        db = LibreriaDB(this)

        editTxtUserID = findViewById(R.id.editTxtUserId)
        editTxtBook1 = findViewById(R.id.editTxtISBNBook1)
        editTxtBook2 = findViewById(R.id.editTxtISBNBook2)

        btnScanUID = findViewById(R.id.btnReturnUID)
        btnScanBook1 = findViewById(R.id.btnReturnBook1)
        btnScanBook2 = findViewById(R.id.btnReturnBook2)

        val returnSubmitButton: Button = findViewById(R.id.btnReturnSubmit)
        returnSubmitButton.setOnClickListener {
            submitReturn()
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

    private fun submitReturn() {
        val userId = editTxtUserID.text.toString()
        val book1ISBN = editTxtBook1.text.toString()
        val book2ISBN = editTxtBook2.text.toString()

        // Assuming return date is the current date
        val returnDate = getCurrentDateTime()

        // Update the return date in the database
        db.updateReturnDate(userId, book1ISBN, book2ISBN, returnDate)

        // Redirect to the MainActivity and open the LendingFragment
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("fragmentToOpen", "LendingFragment")
        }
        startActivity(intent)

        // Finish the activity
        finish()
    }

    private fun getCurrentDateTime(): String {
        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        codeScanner.onRequestPermissionsResult(requestCode, grantResults)
    }
}
