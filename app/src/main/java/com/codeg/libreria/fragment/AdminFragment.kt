package com.codeg.libreria.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeg.libreria.assest.LibreriaDB
import com.codeg.libreria.R
import com.codeg.libreria.activity.AddAdminActivity
import com.codeg.libreria.activity.LoginActivity
import com.codeg.libreria.adapter.AdminAdapter
import kotlinx.android.synthetic.main.fragment_admin.*

class AdminFragment : Fragment() {

    private lateinit var db: LibreriaDB
    private lateinit var adminAdapter: AdminAdapter
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = LibreriaDB(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        recyclerViewAdmin.layoutManager = LinearLayoutManager(requireContext())
        adminAdapter = AdminAdapter()
        recyclerViewAdmin.adapter = adminAdapter

        loadAdmins()
        loadCurrentAdmin()

        addAdminButton.setOnClickListener {
            // Open the activity to add a new admin
            val intent = Intent(requireContext(), AddAdminActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            logout()
        }
    }

    private fun loadAdmins() {
        val admins = db.getAllAdmins()
        adminAdapter.submitList(admins)
    }

    private fun loadCurrentAdmin() {
        val currentAdminId = sharedPreferences.getInt("currentAdminId", -1)
        if (currentAdminId != -1) {
            val currentAdmin = db.getAdminById(currentAdminId)
            textViewCurrentAdmin.text = currentAdmin?.name ?: "No admin logged in"
        } else {
            textViewCurrentAdmin.text = "No admin logged in"
        }
    }

    private fun logout() {
        // Clear user session data
        val editor = sharedPreferences.edit()
        editor.remove("lastLoginTime")
        editor.remove("currentAdminId")
        editor.apply()

        // Navigate to the LoginActivity
        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Finish the MainActivity to prevent going back
    }
}
