package com.codeg.libreria

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val genre: String,
    val numOfCopies: Int,
    val availabilityStatus: String
)
