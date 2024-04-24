package com.codeg.libreria.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.codeg.libreria.assest.LibreriaDB
import com.codeg.libreria.R
import com.codeg.libreria.activity.AddUserActivity
import com.codeg.libreria.adapter.UserAdapter
import kotlinx.android.synthetic.main.fragment_user.addUserButton
import kotlinx.android.synthetic.main.fragment_user.recyclerViewUser

class UserFragment : Fragment() {

    private lateinit var db: LibreriaDB
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = LibreriaDB(requireContext())

        recyclerViewUser.layoutManager = LinearLayoutManager(requireContext())
        userAdapter = UserAdapter()
        recyclerViewUser.adapter = userAdapter

        loadUsers()

        addUserButton.setOnClickListener {
            // Open the activity to add a new user
            val intent = Intent(requireContext(), AddUserActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadUsers() {
        val users = db.getAllUsers()
        userAdapter.submitList(users)
    }
}
