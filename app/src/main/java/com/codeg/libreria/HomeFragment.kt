package com.codeg.libreria

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_home.*

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

        // Retrieve count of books from database
        val bookCount = db.getBooksCount()

        // Retrieve count of copies from database
        val copiesCount = db.getTotalCopiesCount()

        // Set the counts to their respective TextViews
        txtViewBook.text = bookCount.toString()
        txtViewCopies.text = copiesCount.toString()
    }
}
