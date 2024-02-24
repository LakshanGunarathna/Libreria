package com.codeg.libreria

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class AddBookActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTxtTitle: EditText
    private lateinit var editTxtAuthor: EditText
    private lateinit var editTxtISBN: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        editTxtTitle = findViewById(R.id.editTxtTitle)
        editTxtAuthor = findViewById(R.id.editTxtAuthor)
        editTxtISBN = findViewById(R.id.editTxtISBN)

        // Create an instance of CodeScanner with the application context
        codeScanner = CodeScanner(this)
    }

    // Inside onScanButtonClicked function
    fun onScanButtonClicked(view: View) {
        // Start scanning using the CodeScanner instance
        codeScanner.startScanning(
            callback = { result ->
                runOnUiThread {
                    // Update UI on the main thread
                    editTxtISBN.setText(result)  // Use setText instead of text if needed
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