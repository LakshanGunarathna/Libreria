package com.codeg.libreria

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

class AddUserActivity : AppCompatActivity() {

    private lateinit var qrcodeImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)

        qrcodeImageView = findViewById(R.id.qrcodeImageView)
        val inputTxt: EditText = findViewById(R.id.editTxtInput)
        val generateButton: Button = findViewById(R.id.generateButton)
        generateButton.setOnClickListener {
            generateQRCode(inputTxt.text.toString())
        }
    }

    private fun generateQRCode(data: String) {
        val multiFormatWriter = MultiFormatWriter()
        try {
            val bitMatrix: BitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500)
            val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)

            for (i in 0 until 500) {
                for (j in 0 until 500) {
                    bitmap.setPixel(i, j, if (bitMatrix[i, j]) resources.getColor(R.color.black) else resources.getColor(R.color.white))
                }
            }

            qrcodeImageView.setImageBitmap(bitmap)

        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
