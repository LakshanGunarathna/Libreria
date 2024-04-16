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
import androidx.appcompat.app.AppCompatActivity
import com.codeg.libreria.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.io.ByteArrayOutputStream
import java.util.*

class AddUserActivity : AppCompatActivity() {

    private lateinit var qrcodeImageView: ImageView
    private lateinit var userIdEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var telNoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var generateButton: Button
    private lateinit var addUserButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        qrcodeImageView = findViewById(R.id.qrcodeImageView)
        userIdEditText = findViewById(R.id.editTxtUserId)
        nameEditText = findViewById(R.id.editTxtName)
        addressEditText = findViewById(R.id.editTxtAddress)
        telNoEditText = findViewById(R.id.editTxtTelNo)
        emailEditText = findViewById(R.id.emailEditText)
        generateButton = findViewById(R.id.generateButton)
        addUserButton = findViewById(R.id.btnAddUser)

        // Automatically generate a unique user ID
        userIdEditText.setText(UUID.randomUUID().toString())

        generateButton.setOnClickListener {
            generateQRCode(userIdEditText.text.toString())
        }

        addUserButton.setOnClickListener {
            // Implement the logic to add the user to the database
            // After successfully adding the user, make the addUserButton visible
            // For demonstration purposes, I'm making it visible immediately after generating the QR code
            addUserButton.visibility = Button.VISIBLE
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
