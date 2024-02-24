package com.codeg.libreria

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class AddBookActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTextResult: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        editTextResult = findViewById(R.id.editTxtISBN)

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
                    editTextResult.setText(result)  // Use setText instead of text if needed
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

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        codeScanner.onRequestPermissionsResult(requestCode, grantResults)
    }


}