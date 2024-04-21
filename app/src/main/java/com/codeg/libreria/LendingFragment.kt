package com.codeg.libreria

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_books.*
import kotlinx.android.synthetic.main.fragment_lending.addLendingButton

class LendingFragment : Fragment() {

    private lateinit var db: LibreriaDB
    private lateinit var bookAdapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = LibreriaDB(requireContext())

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookAdapter = BookAdapter()
        recyclerView.adapter = bookAdapter

        loadBooks()

        addLendingButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddLendingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadBooks() {
        val books = db.getAllBooks()
        bookAdapter.submitList(books)
    }
}
