package com.codeg.libreria

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_lending.*

class LendingFragment : Fragment() {

    private lateinit var db: LibreriaDB
    private lateinit var lendingAdapter: LendingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_lending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = LibreriaDB(requireContext())

        recyclerViewLending.layoutManager = LinearLayoutManager(requireContext())
        lendingAdapter = LendingAdapter()
        recyclerViewLending.adapter = lendingAdapter

        loadLendings()

        addLendingButton.setOnClickListener {
            val intent = Intent(requireActivity(), AddLendingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadLendings() {
        val lendings = db.getAllLendings()
        lendingAdapter.submitList(lendings)
    }
}
