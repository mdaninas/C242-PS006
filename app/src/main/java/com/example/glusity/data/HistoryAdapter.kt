package com.example.glusity.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.glusity.R

data class HistoryItem(val title: String, val description: String)

class HistoryAdapter(
  private val context: Context,
  private val items: List<HistoryItem>
) : ArrayAdapter<HistoryItem>(context, 0, items) {

  override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
    val item = getItem(position)
    val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_list_card, parent, false)
    val titleTextView = view.findViewById<TextView>(R.id.tv_title)
    val descriptionTextView = view.findViewById<TextView>(R.id.tv_description)
    titleTextView.text = item?.title
    descriptionTextView.text = item?.description

    return view
  }
}