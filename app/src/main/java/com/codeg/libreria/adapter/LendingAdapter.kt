package com.codeg.libreria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeg.libreria.R
import com.codeg.libreria.classfile.Lending
import java.text.SimpleDateFormat
import java.util.*

class LendingAdapter : ListAdapter<Lending, LendingAdapter.LendingViewHolder>(LendingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LendingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lending, parent, false)
        return LendingViewHolder(view)
    }

    override fun onBindViewHolder(holder: LendingViewHolder, position: Int) {
        val lending = getItem(position)
        holder.bind(lending)
    }

    inner class LendingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val borrowingIdTextView: TextView = itemView.findViewById(R.id.txtBorrowingId)
        private val userIdTextView: TextView = itemView.findViewById(R.id.txtUserId)
        private val book1ISBNTextView: TextView = itemView.findViewById(R.id.txtBook1)
        private val book2ISBNTextView: TextView = itemView.findViewById(R.id.txtBook2)
        private val borrowingDateTextView: TextView = itemView.findViewById(R.id.txtBorrowingDate)
        private val dueDateTextView: TextView = itemView.findViewById(R.id.txtDueDate)
        private val returnDateTextView: TextView = itemView.findViewById(R.id.txtReturnDate)

        fun bind(lending: Lending) {
            borrowingIdTextView.text = lending.borrowingId.toString()
            userIdTextView.text = lending.userId
            book1ISBNTextView.text = lending.book1ISBN
            book2ISBNTextView.text = lending.book2ISBN ?: "-" // Show "-" if book2 ISBN is null

            val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val dateFormatterOutput = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

            borrowingDateTextView.text = formatDate(lending.borrowingDate, dateFormatter, dateFormatterOutput)
            dueDateTextView.text = formatDate(lending.dueDate, dateFormatter, dateFormatterOutput)
            returnDateTextView.text = lending.returnDate?.let { formatDate(it, dateFormatter, dateFormatterOutput) } ?: "-"
        }

        private fun formatDate(dateString: String, inputFormatter: SimpleDateFormat, outputFormatter: SimpleDateFormat): String {
            val date = inputFormatter.parse(dateString)
            return outputFormatter.format(date)
        }
    }

    private class LendingDiffCallback : DiffUtil.ItemCallback<Lending>() {
        override fun areItemsTheSame(oldItem: Lending, newItem: Lending): Boolean {
            return oldItem.borrowingId == newItem.borrowingId
        }

        override fun areContentsTheSame(oldItem: Lending, newItem: Lending): Boolean {
            return oldItem == newItem
        }
    }
}

