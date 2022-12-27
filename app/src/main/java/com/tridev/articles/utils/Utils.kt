package com.tridev.articles.utils


import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getFormattedDate(date:String):String{
        //2022-12-23T22:18:20Z
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS'Z'", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("dd MMM yyyy,hh:mm a", Locale.ENGLISH)
        val parsedDate: Date? = inputFormat.parse(date)
        return outputFormat.format(parsedDate!!)
    }
}