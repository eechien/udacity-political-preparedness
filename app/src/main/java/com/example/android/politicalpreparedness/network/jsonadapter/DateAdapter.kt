package com.example.android.politicalpreparedness.network.jsonadapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date

private const val DATE_FORMAT = "yyyy-MM-dd"

class DateAdapter {
    @FromJson
    fun dateFromJson (date: String): Date {
        return SimpleDateFormat(DATE_FORMAT).parse(date)
    }

    @ToJson
    fun dateToJson (date: Date): String {
        return SimpleDateFormat(DATE_FORMAT).format(date)
    }
}