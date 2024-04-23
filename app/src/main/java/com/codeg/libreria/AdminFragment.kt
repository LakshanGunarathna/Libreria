package com.codeg.libreria

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment() {

    private lateinit var db: LibreriaDB
    private lateinit var adminAdapter: AdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = LibreriaDB(requireContext())

        recyclerViewAdmin.layoutManager = LinearLayoutManager(requireContext())
        adminAdapter = AdminAdapter()
        recyclerViewAdmin.adapter = adminAdapter

        loadAdmins()

        addAdminButton.setOnClickListener {
            // Open the activity to add a new admin
            val intent = Intent(requireContext(), AddAdminActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadAdmins() {
        val admins = db.getAllAdmins()
        adminAdapter.submitList(admins)
    }
}
