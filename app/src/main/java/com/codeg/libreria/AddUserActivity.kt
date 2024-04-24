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
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream
import java.util.*

class AddUserActivity : AppCompatActivity() {

    private lateinit var qrcodeImageView: ImageView

    private lateinit var db: LibreriaDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        db = LibreriaDB(this)

        val userIDInput: TextView = findViewById(R.id.editTxtUserId)
        val txtUserId: TextView = findViewById(R.id.txtUserId)
        val nameInput: EditText = findViewById(R.id.editTxtName)
        val addressInput: EditText = findViewById(R.id.editTxtAddress)
        val contactInput: EditText = findViewById(R.id.editTxtTelNo)
        val emailInput: EditText = findViewById(R.id.editTxtEmail)
        val generateButton: Button = findViewById(R.id.generateButton)
        val addUserButton: Button = findViewById(R.id.btnAddUser)
        val btnShare: Button = findViewById(R.id.btnShare)

        // Initialize the ImageView
        qrcodeImageView = findViewById(R.id.qrcodeImageView)

        generateButton.setOnClickListener {
            // Generate a new user ID
            val userId = db.generateNextUserId()
            // Set the generated user ID in the name input field
            userIDInput.text = userId
            userIDInput.visibility = TextView.VISIBLE
            txtUserId.visibility = TextView.VISIBLE

            // Generate QR code logic
            generateQRCode(userId)
            // Enable the addUserButton
            addUserButton.visibility = Button.VISIBLE
            btnShare.visibility = Button.VISIBLE
            generateButton.visibility= Button.GONE
        }

        val cancelButton: Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous screen
        }

        addUserButton.setOnClickListener {
            val userId = userIDInput.text.toString()
            val name = nameInput.text.toString()
            val address = addressInput.text.toString()
            val contact = contactInput.text.toString()
            val email = emailInput.text.toString()

            // Insert user data into the database
            db.insertUserData(userId, name, address, contact, email)

            // Show a toast message upon successful insertion
            Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()

            // Clear input fields after adding the user
            nameInput.text.clear()
            addressInput.text.clear()
            contactInput.text.clear()
            emailInput.text.clear()

            // Redirect to the MainActivity and open the BooksFragment
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("fragmentToOpen", "UserFragment")
            }
            startActivity(intent)

            // Finish the current activity
            finish()
        }

        // Share button functionality
        val shareButton: Button = findViewById(R.id.btnShare)
        shareButton.setOnClickListener {
            shareQRCode()
        }
    }

    private fun generateQRCode(data: String) {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500)
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)

            for (i in 0 until 500) {
                for (j in 0 until 500) {
                    bitmap.setPixel(
                        i,
                        j,
                        if (bitMatrix[i, j]) resources.getColor(R.color.black) else resources.getColor(
                            R.color.white
                        )
                    )
                }
            }

            qrcodeImageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun shareQRCode() {
        val bitmap = (qrcodeImageView.drawable as BitmapDrawable).bitmap
        val imageUri: Uri = getImageUri(bitmap)

        // Share the image via Intent
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, imageUri)
            type = "image/jpeg"
        }
        startActivity(Intent.createChooser(shareIntent, "Share QR Code via"))
    }

    // Helper method to get image URI
    private fun getImageUri(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            contentResolver,
            bitmap,
            "QR_Code_${System.currentTimeMillis()}",
            null
        )
        return Uri.parse(path)
    }
}
