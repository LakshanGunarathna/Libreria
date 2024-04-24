package com.codeg.libreria.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import com.codeg.libreria.assest.CodeScanner
import com.codeg.libreria.assest.LibreriaDB
import com.codeg.libreria.R

class AddBookActivity : AppCompatActivity() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var editTxtTitle: EditText
    private lateinit var editTxtAuthor: EditText
    private lateinit var editTxtISBN: EditText
    private lateinit var editTxtNoCopies: EditText
    private lateinit var spinnerGenre: Spinner
    private lateinit var db: LibreriaDB
    private lateinit var imageViewBookCover: ImageView

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        editTxtTitle = findViewById(R.id.editTxtTitle)
        editTxtAuthor = findViewById(R.id.editTxtAuthor)
        editTxtISBN = findViewById(R.id.editTxtISBN)
        spinnerGenre = findViewById(R.id.spinnerGenre)
        editTxtNoCopies = findViewById(R.id.editTxtNoCopies)
        imageViewBookCover = findViewById(R.id.imageViewBookCover)

        // Create an instance of CodeScanner with the application context
        codeScanner = CodeScanner(this)

        db = LibreriaDB(this)

        val selectImageButton = findViewById<Button>(R.id.btnSelectCover)
        selectImageButton.setOnClickListener{
            openImageChooser()
        }

        val saveButton = findViewById<Button>(R.id.btnAddBook)
        saveButton.setOnClickListener {
            saveBook()
        }

        val cancelButton: Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous screen
        }

        // Set up the spinner for genre selection
        ArrayAdapter.createFromResource(
            this,
            R.array.genre,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerGenre.adapter = adapter
        }

        // Set up click listener for the image view
        imageViewBookCover.setOnClickListener {
            openImageChooser()
        }
    }

    fun onScanButtonClicked(view: View) {
        // Start scanning using the CodeScanner instance
        codeScanner.startScanning(
            callback = { result ->
                runOnUiThread {
                    // Update UI on the main thread
                    editTxtISBN.setText(result)
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
    private fun openImageChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImage = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImage)
            imageViewBookCover.setImageBitmap(bitmap)
        }
    }

    private fun saveBook() {
        val isbn = editTxtISBN.text.toString()
        val title = editTxtTitle.text.toString()
        val author = editTxtAuthor.text.toString()
        val genre = spinnerGenre.selectedItem.toString()
        val numOfCopies = editTxtNoCopies.text.toString().toIntOrNull() ?: 0

        // Get the bitmap from the ImageView
        val bitmap = (imageViewBookCover.drawable as BitmapDrawable).bitmap

        // Add book data to the database
        db.insertBookData(isbn, title, author, genre, numOfCopies, "Available", this, bitmap)

        // Redirect to the MainActivity and open the BooksFragment
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("fragmentToOpen", "BooksFragment")
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