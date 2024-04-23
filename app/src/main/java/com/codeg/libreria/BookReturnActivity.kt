package com.codeg.libreria

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.codeg.libreria.LibreriaDB
import java.text.SimpleDateFormat
import java.util.*

class BookReturnActivity : AppCompatActivity() {

    private lateinit var db: LibreriaDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_return)

        db = LibreriaDB(this)

        val returnSubmitButton: Button = findViewById(R.id.btnReturnSubmit)
        returnSubmitButton.setOnClickListener {
            submitReturn()
        }

        val cancelButton: Button = findViewById(R.id.btnCancel)
        cancelButton.setOnClickListener {
            finish() // Finish the activity and go back to the previous screen
        }
    }

    private fun submitReturn() {
        val userId = findViewById<EditText>(R.id.editTxtUserId).text.toString()
        val book1ISBN = findViewById<EditText>(R.id.editTxtISBNBook1).text.toString()
        val book2ISBN = findViewById<EditText>(R.id.editTxtISBNBook2).text.toString()

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
}
