package com.codeg.libreria.classfile

data class Lending(
    val borrowingId: Int,
    val userId: String,
    val book1ISBN: String,
    val book2ISBN: String?,
    val borrowingDate: String, // Keep as String
    val dueDate: String, // Keep as String
    val returnDate: String? // Keep as String
)

