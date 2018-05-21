package com.arctouch.codechallenge.extensions

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@JvmOverloads
fun String.convertToFormattedDate(language: String? = null): String{

    val lang: String = language ?: Locale.getDefault().displayLanguage

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    try {
        val newDate = sdf.parse(this)

        val sdfLocal = if(lang.contentEquals("português")){
            SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
        } else{
            SimpleDateFormat("MM/dd/yyyy", Locale.ROOT)
        }

        return sdfLocal.format(newDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return ""

}