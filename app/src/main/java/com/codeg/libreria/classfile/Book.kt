package com.codeg.libreria.classfile

import android.graphics.Bitmap

data class Book(
    val isbn: String,
    val title: String,
    val author: String,
    val genre: String,
    val numOfCopies: Int,
    val availabilityStatus: String,
    val imageBitmap: Bitmap?
)
