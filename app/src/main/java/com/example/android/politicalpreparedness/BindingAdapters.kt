package com.example.android.politicalpreparedness

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.network.models.Division
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.model.Representative
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("dateText")
fun dateToString(textView: TextView, date: Date?) {
    if (date != null) {
        textView.text = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").format(date)
    }
}
