package com.translator.wordwanderer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter
import com.translator.wordwanderer.Model.Translation

class TranslationAdapter(private val context: Context, private val dataSource: List<Translation>) : BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_translation, parent, false)

        val englishTextView = rowView.findViewById<TextView>(R.id.englishText)
        val tagalogTextView = rowView.findViewById<TextView>(R.id.tagalogText)

        val translation = getItem(position) as Translation

        englishTextView.text = translation.english
        tagalogTextView.text = translation.tagalog

        return rowView
    }
}
