package com.codeg.libreria

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.codeg.libreria.Book
import com.codeg.libreria.R

class BookAdapter : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        holder.bind(book)
    }

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txtViewTitle)
        private val authorTextView: TextView = itemView.findViewById(R.id.txtViewAuthor)
        private val isbnTextView: TextView = itemView.findViewById(R.id.txtViewIsbn)
        private val genreTextView: TextView = itemView.findViewById(R.id.txtViewGenre)
        private val copiesTextView: TextView = itemView.findViewById(R.id.txtViewNoCopies)
        private val coverImageView: ImageView = itemView.findViewById(R.id.imageViewCover)

        fun bind(book: Book) {
            titleTextView.text = book.title
            authorTextView.text = book.author
            isbnTextView.text = book.isbn
            genreTextView.text = book.genre
            copiesTextView.text = book.numOfCopies.toString()
            coverImageView.setImageBitmap(book.imageBitmap)
        }
    }

    private class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.isbn == newItem.isbn
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}
