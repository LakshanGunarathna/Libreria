package com.codeg.libreria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(UserDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userIdTextView: TextView = itemView.findViewById(R.id.txtViewUserId)
        private val nameTextView: TextView = itemView.findViewById(R.id.txtViewName)
        private val addressTextView: TextView = itemView.findViewById(R.id.txtViewAddress)
        private val contactTextView: TextView = itemView.findViewById(R.id.txtViewContact)
        private val emailTextView: TextView = itemView.findViewById(R.id.txtViewEmail)

        fun bind(user: User) {
            userIdTextView.text = user.userId.toString() // Convert Int to String explicitly
            nameTextView.text = user.name
            addressTextView.text = user.address
            contactTextView.text = user.contact
            emailTextView.text = user.email
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }
}
