package com.codeg.libreria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeg.libreria.R
import com.codeg.libreria.classfile.Admin

class AdminAdapter : ListAdapter<Admin, AdminAdapter.AdminViewHolder>(AdminDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin, parent, false)
        return AdminViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        val admin = getItem(position)
        holder.bind(admin)
    }

    inner class AdminViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.txtViewAdminName)
        private val addressTextView: TextView = itemView.findViewById(R.id.txtViewAdminAddress)
        private val telNoTextView: TextView = itemView.findViewById(R.id.txtViewAdminContact)
        private val emailTextView: TextView = itemView.findViewById(R.id.txtViewAdminEmail)

        fun bind(admin: Admin) {
            nameTextView.text = admin.name
            addressTextView.text = admin.address
            telNoTextView.text = admin.telNo
            emailTextView.text = admin.email
        }
    }

    private class AdminDiffCallback : DiffUtil.ItemCallback<Admin>() {
        override fun areItemsTheSame(oldItem: Admin, newItem: Admin): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Admin, newItem: Admin): Boolean {
            return oldItem == newItem
        }
    }
}
