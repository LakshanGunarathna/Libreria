package com.codeg.libreria.assest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.codeg.libreria.classfile.Admin
import com.codeg.libreria.classfile.Book
import com.codeg.libreria.classfile.Lending
import com.codeg.libreria.classfile.User
import java.io.ByteArrayOutputStream
import java.util.Locale

class LibreriaDB(context: Context) : SQLiteOpenHelper(context, "libreria_db", null, 1) {
    companion object {
        const val TABLE_USERS = "Users"
        const val TABLE_BOOKS = "Books"
        const val TABLE_BORROWINGS = "Borrowings"
        const val TABLE_ADMINS = "Admins"

        // Define columns for Users table
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USER_NAME = "user_name"
        const val COLUMN_USER_ADDRESS = "user_address"
        const val COLUMN_USER_CONTACT = "user_contact"
        const val COLUMN_USER_EMAIL = "user_email"

        // Define columns for Books table
        const val COLUMN_BOOK_ISBN = "book_isbn"
        const val COLUMN_BOOK_TITLE = "book_title"
        const val COLUMN_BOOK_AUTHOR = "book_author"
        const val COLUMN_BOOK_GENRE = "book_genre"
        const val COLUMN_BOOK_NUM_OF_COPIES = "book_num_of_copies"
        const val COLUMN_BOOK_COVER_IMAGE = "book_cover_image"
        const val COLUMN_BOOK_AVAILABILITY_STATUS = "book_availability_status"

        // Define columns for Borrowings table
        const val COLUMN_BORROWING_ID = "borrowing_id"
        const val COLUMN_BORROWING_USER_ID = "borrowing_user_id"
        const val COLUMN_BORROWING_BOOK1 = "borrowing_book1"
        const val COLUMN_BORROWING_BOOK2 = "borrowing_book2"
        const val COLUMN_BORROWING_DATE = "borrowing_date"
        const val COLUMN_BORROWING_DUE_DATE = "borrowing_due_date"
        const val COLUMN_BORROWING_RETURN_DATE = "borrowing_return_date"

        // Define columns for Admins table
        const val COLUMN_ADMIN_ID = "admin_id"
        const val COLUMN_ADMIN_NAME = "admin_name"
        const val COLUMN_ADMIN_ADDRESS = "admin_address"
        const val COLUMN_ADMIN_TELEPHONE = "admin_contact"
        const val COLUMN_ADMIN_EMAIL = "admin_email"
        const val COLUMN_ADMIN_USERNAME = "admin_username"
        const val COLUMN_ADMIN_PASSWORD = "admin_password"

        // SQL statement for creating Users table
        const val SQL_CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID TEXT PRIMARY KEY,
                $COLUMN_USER_NAME TEXT,
                $COLUMN_USER_ADDRESS TEXT,
                $COLUMN_USER_CONTACT TEXT,
                $COLUMN_USER_EMAIL TEXT
            )
        """

        // SQL statement for creating Books table
        const val SQL_CREATE_BOOKS_TABLE = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_BOOK_ISBN TEXT PRIMARY KEY,
                $COLUMN_BOOK_TITLE TEXT,
                $COLUMN_BOOK_AUTHOR TEXT,
                $COLUMN_BOOK_GENRE TEXT,
                $COLUMN_BOOK_NUM_OF_COPIES INTEGER,
                $COLUMN_BOOK_AVAILABILITY_STATUS TEXT,
                $COLUMN_BOOK_COVER_IMAGE TEXT
            )
        """

        const val SQL_CREATE_BORROWINGS_TABLE = """
            CREATE TABLE $TABLE_BORROWINGS (
                $COLUMN_BORROWING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BORROWING_USER_ID TEXT,
                $COLUMN_BORROWING_BOOK1 TEXT,
                $COLUMN_BORROWING_BOOK2 TEXT,
                $COLUMN_BORROWING_DATE TEXT,
                $COLUMN_BORROWING_DUE_DATE TEXT,
                $COLUMN_BORROWING_RETURN_DATE TEXT
            )
        """


        const val SQL_CREATE_ADMINS_TABLE = """
            CREATE TABLE $TABLE_ADMINS (
                $COLUMN_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ADMIN_USERNAME TEXT,
                $COLUMN_ADMIN_PASSWORD TEXT,
                $COLUMN_ADMIN_NAME TEXT,
                $COLUMN_ADMIN_ADDRESS TEXT,
                $COLUMN_ADMIN_TELEPHONE TEXT,
                $COLUMN_ADMIN_EMAIL TEXT
    )
"""

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_USERS_TABLE)
        db.execSQL(SQL_CREATE_BOOKS_TABLE)
        db.execSQL(SQL_CREATE_BORROWINGS_TABLE)
        db.execSQL(SQL_CREATE_ADMINS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BORROWINGS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ADMINS")
        onCreate(db)
    }


    // Insert user data into Users table
    fun insertUserData(userId: String, name: String, address: String, contact: String, email: String) {
        val values = ContentValues().apply {
            put(COLUMN_USER_ID, userId) // Save the provided user ID
            put(COLUMN_USER_NAME, name)
            put(COLUMN_USER_ADDRESS, address)
            put(COLUMN_USER_CONTACT, contact)
            put(COLUMN_USER_EMAIL, email)
        }

        val db = writableDatabase
        db.insert(TABLE_USERS, null, values)
        db.close()
    }
    // Insert book data into Books table
    fun insertBookData(
        isbn: String,
        title: String,
        author: String,
        genre: String,
        numOfCopies: Int,
        availabilityStatus: String,
        context: Context,
        coverBitmap: Bitmap? // Updated parameter to accept Bitmap
    ) {
        val values = ContentValues().apply {
            put(COLUMN_BOOK_ISBN, isbn)
            put(COLUMN_BOOK_TITLE, title)
            put(COLUMN_BOOK_AUTHOR, author)
            put(COLUMN_BOOK_GENRE, genre)
            put(COLUMN_BOOK_NUM_OF_COPIES, numOfCopies)
            put(COLUMN_BOOK_AVAILABILITY_STATUS, availabilityStatus)
            // Store the bitmap as byte array in the database
            put(COLUMN_BOOK_COVER_IMAGE, coverBitmap?.let { convertBitmapToByteArray(it) })

        }

        val db = writableDatabase
        val result = db.insert(TABLE_BOOKS, null, values)
        if (result == -1L) {
            // Insert failed
            Toast.makeText(context, "Failed to add book", Toast.LENGTH_SHORT).show()
        } else {
            // Insert successful
            Toast.makeText(context, "Book added successfully", Toast.LENGTH_SHORT).show()
        }
    }

    // Helper method to convert Bitmap to byte array
    private fun convertBitmapToByteArray(bitmap: Bitmap?): ByteArray? {
        if (bitmap == null) return null
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_BOOKS", null)
        cursor?.use { cursor ->
            val isbnIndex = cursor.getColumnIndex(COLUMN_BOOK_ISBN)
            val titleIndex = cursor.getColumnIndex(COLUMN_BOOK_TITLE)
            val authorIndex = cursor.getColumnIndex(COLUMN_BOOK_AUTHOR)
            val genreIndex = cursor.getColumnIndex(COLUMN_BOOK_GENRE)
            val numOfCopiesIndex = cursor.getColumnIndex(COLUMN_BOOK_NUM_OF_COPIES)
            val availabilityStatusIndex = cursor.getColumnIndex(COLUMN_BOOK_AVAILABILITY_STATUS)
            val imageBitmapIndex = cursor.getColumnIndex(COLUMN_BOOK_COVER_IMAGE) // Add this line

            while (cursor.moveToNext()) {
                val isbn = cursor.getString(isbnIndex)
                val title = cursor.getString(titleIndex)
                val author = cursor.getString(authorIndex)
                val genre = cursor.getString(genreIndex)
                val numOfCopies = cursor.getInt(numOfCopiesIndex)
                val availabilityStatus = cursor.getString(availabilityStatusIndex)
                val imageByteArray = cursor.getBlob(imageBitmapIndex) // Update this line
                val imageBitmap = if (imageByteArray != null) BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size) else null // Update this line

                val book = Book(isbn, title, author, genre, numOfCopies, availabilityStatus, imageBitmap)
                books.add(book)
            }
        }
        return books
    }




    fun getBooksCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_BOOKS", null)
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        cursor?.close()
        return count
    }

    fun getTotalCopiesCount(): Int {
        val db = readableDatabase
        val query = "SELECT SUM($COLUMN_BOOK_NUM_OF_COPIES) FROM $TABLE_BOOKS"
        val cursor = db.rawQuery(query, null)
        var count = 0

        cursor?.let {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
            it.close()
        }

        return count
    }

    // Get all users from Users table
    fun getAllUsers(): ArrayList<User> {
        val usersList = ArrayList<User>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_USERS", null)
        cursor?.use {
            if (it.moveToFirst()) {
                val idIndex = it.getColumnIndex(COLUMN_USER_ID)
                val nameIndex = it.getColumnIndex(COLUMN_USER_NAME)
                val addressIndex = it.getColumnIndex(COLUMN_USER_ADDRESS)
                val contactIndex = it.getColumnIndex(COLUMN_USER_CONTACT)
                val emailIndex = it.getColumnIndex(COLUMN_USER_EMAIL)

                do {
                    val userId = it.getString(idIndex)
                    val name = it.getString(nameIndex)
                    val address = it.getString(addressIndex)
                    val contact = it.getString(contactIndex)
                    val email = it.getString(emailIndex)

                    val user = User(userId, name, address, contact, email)
                    usersList.add(user)
                } while (it.moveToNext())
            }
        }
        cursor?.close()
        db.close()
        return usersList
    }



    // Get the count of users in Users table
    fun getUsersCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_USERS", null)
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return count
    }


    fun generateNextUserId(): String {
        val lastUserId = getLastUserId()
        val nextId = if (lastUserId != null) {
            val lastNumber = lastUserId.substring(2).toInt()
            String.format(Locale.US, "LU%03d", lastNumber + 1)
        } else {
            "LU001" // If no users exist, start with LU001
        }
        return nextId
    }


    @SuppressLint("Range")
    private fun getLastUserId(): String? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_USER_ID FROM $TABLE_USERS ORDER BY $COLUMN_USER_ID DESC LIMIT 1", null)
        var lastUserId: String? = null
        if (cursor != null && cursor.moveToFirst()) {
            lastUserId = cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))
        }
        cursor?.close()
        return lastUserId
    }


    // Insert borrowing data into Borrowings table
    fun insertBorrowingData(userId: String, book1: String, book2: String?, borrowingDate: String, dueDate: String, returnDate: String?) {
        val values = ContentValues().apply {
            put(COLUMN_BORROWING_USER_ID, userId)
            put(COLUMN_BORROWING_BOOK1, book1)
            put(COLUMN_BORROWING_BOOK2, book2)
            put(COLUMN_BORROWING_DATE, borrowingDate)
            put(COLUMN_BORROWING_DUE_DATE, dueDate)
            put(COLUMN_BORROWING_RETURN_DATE, returnDate)
        }

        val db = writableDatabase
        db.insert(TABLE_BORROWINGS, null, values)
    }


    // Retrieve all lendings from Borrowings table
    fun getAllLendings(): List<Lending> {
        val lendings = mutableListOf<Lending>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_BORROWINGS", null)
        cursor?.use { cursor ->
            val borrowingIdIndex = cursor.getColumnIndex(COLUMN_BORROWING_ID)
            val userIdIndex = cursor.getColumnIndex(COLUMN_BORROWING_USER_ID)
            val book1ISBNIndex = cursor.getColumnIndex(COLUMN_BORROWING_BOOK1)
            val book2ISBNIndex = cursor.getColumnIndex(COLUMN_BORROWING_BOOK2)
            val borrowingDateIndex = cursor.getColumnIndex(COLUMN_BORROWING_DATE)
            val dueDateIndex = cursor.getColumnIndex(COLUMN_BORROWING_DUE_DATE)
            val returnDateIndex = cursor.getColumnIndex(COLUMN_BORROWING_RETURN_DATE)

            while (cursor.moveToNext()) {
                val borrowingId = cursor.getInt(borrowingIdIndex)
                val userId = cursor.getString(userIdIndex)
                val book1ISBN = cursor.getString(book1ISBNIndex)
                val book2ISBN = cursor.getString(book2ISBNIndex)
                val borrowingDate = cursor.getString(borrowingDateIndex)
                val dueDate = cursor.getString(dueDateIndex)
                val returnDate = cursor.getString(returnDateIndex)

                val lending = Lending(borrowingId, userId, book1ISBN, book2ISBN, borrowingDate, dueDate, returnDate)
                lendings.add(lending)
            }
        }
        return lendings
    }

    // Assuming this function is inside your LibreriaDB class

    fun updateReturnDate(userId: String, book1ISBN: String, book2ISBN: String?, returnDate: String) {
        val db = writableDatabase

        // Check both combinations of book ISBNs
        val values = ContentValues().apply {
            put(COLUMN_BORROWING_RETURN_DATE, returnDate)
        }

        val selection1 = "$COLUMN_BORROWING_USER_ID = ? AND $COLUMN_BORROWING_BOOK1 = ? AND $COLUMN_BORROWING_BOOK2 = ?"
        val selectionArgs1 = arrayOf(userId, book1ISBN, book2ISBN ?: "")

        val selection2 = "$COLUMN_BORROWING_USER_ID = ? AND $COLUMN_BORROWING_BOOK1 = ? AND $COLUMN_BORROWING_BOOK2 = ?"
        val selectionArgs2 = arrayOf(userId, book2ISBN ?: "", book1ISBN)

        // Update return date if found in either combination
        if (db.update(TABLE_BORROWINGS, values, selection1, selectionArgs1) == 0) {
            db.update(TABLE_BORROWINGS, values, selection2, selectionArgs2)
        }
    }



    // Calculate the total number of borrowed books
    fun getTotalBorrowedBooks(): Int {
        val lendings = getAllLendings()
        var totalBorrowedBooks = 0
        for (lending in lendings) {
            // Check if the lending has been returned
            if (lending.returnDate == null) {
                // Increment total borrowed books by 1 for each book borrowed in the lending
                totalBorrowedBooks += if (lending.book2ISBN != null) 2 else 1
            }
        }
        return totalBorrowedBooks
    }


    // Calculate the number of successfully returned lendings
    fun getReturnedLendingsCount(): Int {
        val lendings = getAllLendings()
        var successfullyReturnedCount = 0
        for (lending in lendings) {
            if (lending.returnDate != null) {
                // Increment count if the lending has a return date
                successfullyReturnedCount++
            }
        }
        return successfullyReturnedCount
    }

    fun insertAdminData(
        username: String,
        password: String,
        name: String,
        address: String,
        telephone: String,
        email: String
    ) {
        val values = ContentValues().apply {
            put(COLUMN_ADMIN_USERNAME, username)
            put(COLUMN_ADMIN_PASSWORD, password)
            put(COLUMN_ADMIN_NAME, name)
            put(COLUMN_ADMIN_ADDRESS, address)
            put(COLUMN_ADMIN_TELEPHONE, telephone)
            put(COLUMN_ADMIN_EMAIL, email)
        }

        val db = writableDatabase
        db.insert(TABLE_ADMINS, null, values)
        db.close()
    }

    fun getAllAdmins(): List<Admin> {
        val admins = mutableListOf<Admin>()
        val db = readableDatabase
        val cursor: Cursor? = db.rawQuery("SELECT * FROM $TABLE_ADMINS", null)
        cursor?.use { cursor ->
            val adminIdIndex = cursor.getColumnIndex(COLUMN_ADMIN_ID)
            val usernameIndex = cursor.getColumnIndex(COLUMN_ADMIN_USERNAME)
            val passwordIndex = cursor.getColumnIndex(COLUMN_ADMIN_PASSWORD)
            val nameIndex = cursor.getColumnIndex(COLUMN_ADMIN_NAME)
            val addressIndex = cursor.getColumnIndex(COLUMN_ADMIN_ADDRESS)
            val telephoneIndex = cursor.getColumnIndex(COLUMN_ADMIN_TELEPHONE)
            val emailIndex = cursor.getColumnIndex(COLUMN_ADMIN_EMAIL)

            while (cursor.moveToNext()) {
                val id = cursor.getInt(adminIdIndex)
                val username = cursor.getString(usernameIndex)
                val password = cursor.getString(passwordIndex)
                val name = cursor.getString(nameIndex)
                val address = cursor.getString(addressIndex)
                val telephone = cursor.getString(telephoneIndex)
                val email = cursor.getString(emailIndex)

                val admin = Admin(id,username, password, name, address, telephone, email)
                admins.add(admin)
            }
        }
        cursor?.close()
        db.close()
        return admins
    }


    fun getAdminsCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_ADMINS", null)
        var count = 0
        cursor?.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }
        cursor?.close()
        db.close()
        return count
    }

    @SuppressLint("Range")
    fun getAdminByUsernameAndPassword(username: String, password: String): Admin? {
        val db = readableDatabase
        val selection = "$COLUMN_ADMIN_USERNAME = ? AND $COLUMN_ADMIN_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor = db.query(TABLE_ADMINS, null, selection, selectionArgs, null, null, null)
        var admin: Admin? = null

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ADMIN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_NAME))
            val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_ADDRESS))
            val telephone = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_TELEPHONE))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_EMAIL))

            admin = Admin(id, username, password, name, address, telephone, email)
        }

        cursor?.close()
        db.close()
        return admin
    }


    fun adminsExist(): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_ADMINS", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count > 0
    }
    @SuppressLint("Range")
    fun getAdminById(adminId: Int): Admin? {
        val db = readableDatabase
        val selection = "$COLUMN_ADMIN_ID = ?"
        val selectionArgs = arrayOf(adminId.toString())
        val cursor = db.query(TABLE_ADMINS, null, selection, selectionArgs, null, null, null)
        var admin: Admin? = null

        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ADMIN_ID))
            val username = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_USERNAME))
            val password = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_PASSWORD))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_NAME))
            val address = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_ADDRESS))
            val telephone = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_TELEPHONE))
            val email = cursor.getString(cursor.getColumnIndex(COLUMN_ADMIN_EMAIL))

            admin = Admin(id, username, password, name, address, telephone, email)
        }

        cursor?.close()
        db.close()
        return admin
    }


}
