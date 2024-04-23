package com.codeg.libreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize LibreriaDB
        val db = LibreriaDB(requireContext())

        // Retrieve count of admins from database
        val adminCount = db.getAdminsCount()

        // Retrieve count of books from database
        val bookCount = db.getBooksCount()

        // Retrieve count of copies from database
        val copiesCount = db.getTotalCopiesCount()

        // Retrieve count of users from database
        val userCount = db.getUsersCount()

        // Retrieve count of lendings from database
        val lendingCount = db.getAllLendings().size

        // Retrieve count of borrowed books from database
        val borrowedBookCount = db.getTotalBorrowedBooks()

        // Retrieve count of returned lending from database
        val returnedLendingCount = db.getReturnedLendingsCount()

        // Calculate the count of overdue lendings
        val overdueLendingCount = calculateOverdueLendings(db)

        // Set the counts to their respective TextViews
        txtViewAdmin.text = adminCount.toString()
        txtViewBook.text = bookCount.toString()
        txtViewCopies.text = copiesCount.toString()
        txtViewUser.text = userCount.toString()
        txtViewLending.text = lendingCount.toString()
        txtViewOverDue.text = overdueLendingCount.toString()
        txtViewBorrowedBooks.text = borrowedBookCount.toString()
        txtViewReturn.text = returnedLendingCount.toString()
    }

    // Function to calculate the count of overdue lendings
    private fun calculateOverdueLendings(db: LibreriaDB): Int {
        val lendings = db.getAllLendings()
        val currentDate = Calendar.getInstance().time

        var overdueCount = 0
        for (lending in lendings) {
            if (lending.dueDate != null) {
                val dueDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(lending.dueDate)
                if (dueDate != null && currentDate.after(dueDate)) {
                    overdueCount++
                }
            }
        }
        return overdueCount
    }
}
