package com.codeg.libreria

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
        const val COLUMN_USER_LIBRARY_CARD = "user_library_card"

        // Define columns for Books table
        const val COLUMN_BOOK_ISBN = "book_isbn"
        const val COLUMN_BOOK_TITLE = "book_title"
        const val COLUMN_BOOK_AUTHOR = "book_author"
        const val COLUMN_BOOK_GENRE = "book_genre"
        const val COLUMN_BOOK_PUBLICATION_YEAR = "book_publication_year"
        const val COLUMN_BOOK_AVAILABILITY_STATUS = "book_availability_status"

        // Define columns for Borrowings table
        const val COLUMN_BORROWING_ID = "borrowing_id"
        const val COLUMN_BORROWING_USER_ID = "borrowing_user_id"
        const val COLUMN_BORROWING_ISBN = "borrowing_isbn"
        const val COLUMN_BORROWING_DATE = "borrowing_date"
        const val COLUMN_BORROWING_DUE_DATE = "borrowing_due_date"
        const val COLUMN_BORROWING_RETURN_DATE = "borrowing_return_date"

        // Define columns for Admins table
        const val COLUMN_ADMIN_ID = "admin_id"
        const val COLUMN_ADMIN_USERNAME = "admin_username"
        const val COLUMN_ADMIN_PASSWORD = "admin_password"

        // SQL statements for creating tables
        const val SQL_CREATE_USERS_TABLE = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_NAME TEXT,
                $COLUMN_USER_ADDRESS TEXT,
                $COLUMN_USER_CONTACT TEXT,
                $COLUMN_USER_EMAIL TEXT,
                $COLUMN_USER_LIBRARY_CARD TEXT
            )
        """

        const val SQL_CREATE_BOOKS_TABLE = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_BOOK_ISBN TEXT PRIMARY KEY,
                $COLUMN_BOOK_TITLE TEXT,
                $COLUMN_BOOK_AUTHOR TEXT,
                $COLUMN_BOOK_GENRE TEXT,
                $COLUMN_BOOK_PUBLICATION_YEAR INTEGER,
                $COLUMN_BOOK_AVAILABILITY_STATUS TEXT
            )
        """

        const val SQL_CREATE_BORROWINGS_TABLE = """
            CREATE TABLE $TABLE_BORROWINGS (
                $COLUMN_BORROWING_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BORROWING_USER_ID INTEGER,
                $COLUMN_BORROWING_ISBN TEXT,
                $COLUMN_BORROWING_DATE TEXT,
                $COLUMN_BORROWING_DUE_DATE TEXT,
                $COLUMN_BORROWING_RETURN_DATE TEXT
            )
        """

        const val SQL_CREATE_ADMINS_TABLE = """
            CREATE TABLE $TABLE_ADMINS (
                $COLUMN_ADMIN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ADMIN_USERNAME TEXT,
                $COLUMN_ADMIN_PASSWORD TEXT
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
    fun insertUserData(name: String, address: String, contact: String, email: String, libraryCard: String) {
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, name)
            put(COLUMN_USER_ADDRESS, address)
            put(COLUMN_USER_CONTACT, contact)
            put(COLUMN_USER_EMAIL, email)
            put(COLUMN_USER_LIBRARY_CARD, libraryCard)
        }

        val db = writableDatabase
        db.insert(TABLE_USERS, null, values)
    }

    // Insert book data into Books table
    fun insertBookData(isbn: String, title: String, author: String, genre: String, publicationYear: Int, availabilityStatus: String) {
        val values = ContentValues().apply {
            put(COLUMN_BOOK_ISBN, isbn)
            put(COLUMN_BOOK_TITLE, title)
            put(COLUMN_BOOK_AUTHOR, author)
            put(COLUMN_BOOK_GENRE, genre)
            put(COLUMN_BOOK_PUBLICATION_YEAR, publicationYear)
            put(COLUMN_BOOK_AVAILABILITY_STATUS, availabilityStatus)
        }

        val db = writableDatabase
        db.insert(TABLE_BOOKS, null, values)
    }

    // Insert borrowing data into Borrowings table
    fun insertBorrowingData(userId: Int, isbn: String, borrowingDate: String, dueDate: String, returnDate: String) {
        val values = ContentValues().apply {
            put(COLUMN_BORROWING_USER_ID, userId)
            put(COLUMN_BORROWING_ISBN, isbn)
            put(COLUMN_BORROWING_DATE, borrowingDate)
            put(COLUMN_BORROWING_DUE_DATE, dueDate)
            put(COLUMN_BORROWING_RETURN_DATE, returnDate)
        }

        val db = writableDatabase
        db.insert(TABLE_BORROWINGS, null, values)
    }

    // Insert admin data into Admins table
    fun insertAdminData(username: String, password: String) {
        val values = ContentValues().apply {
            put(COLUMN_ADMIN_USERNAME, username)
            put(COLUMN_ADMIN_PASSWORD, password)
        }

        val db = writableDatabase
        db.insert(TABLE_ADMINS, null, values)
    }
}
