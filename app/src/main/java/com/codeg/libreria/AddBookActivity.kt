package com.codeg.libreria

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddBookActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTxtTitle: EditText
    private lateinit var editTxtAuthor: EditText
    private lateinit var editTxtISBN: EditText
    private lateinit var db: LibreriaDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        editTxtTitle = findViewById(R.id.editTxtTitle)
        editTxtAuthor = findViewById(R.id.editTxtAuthor)
        editTxtISBN = findViewById(R.id.editTxtISBN)

        // Create an instance of CodeScanner with the application context
        codeScanner = CodeScanner(this)

        db = LibreriaDB(this)

        val saveButton = findViewById<Button>(R.id.btnAddBook)
        saveButton.setOnClickListener {
            saveBook()
            finish()
        }


        val cancelButton: Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous screen
        }

    }

    // Inside onScanButtonClicked function
    fun onScanButtonClicked(view: View) {
        // Start scanning using the CodeScanner instance
        codeScanner.startScanning(
            callback = { result ->
                runOnUiThread {
                    // Update UI on the main thread
                    editTxtISBN.setText(result)  // Use setText instead of text if needed
                    codeScanner
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

    private fun saveBook() {
        val isbnEditText = findViewById<EditText>(R.id.editTxtISBN)
        val titleEditText = findViewById<EditText>(R.id.editTxtTitle)
        val authorEditText = findViewById<EditText>(R.id.editTxtAuthor)
        val genreEditText = findViewById<EditText>(R.id.editTxtGenre)
        val numOfCopiesEditText = findViewById<EditText>(R.id.editTxtNoCopies)

        val isbn = isbnEditText.text.toString()
        val title = titleEditText.text.toString()
        val author = authorEditText.text.toString()
        val genre = genreEditText.text.toString()
        val numOfCopies = numOfCopiesEditText.text.toString().toInt()

        // Add book data to the database
        db.insertBookData(isbn, title, author, genre, numOfCopies, "Available", this)
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