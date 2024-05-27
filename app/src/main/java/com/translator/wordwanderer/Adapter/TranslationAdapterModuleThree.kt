package com.translator.wordwanderer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.translator.wordwanderer.model.ModuleThreeTranslation
import com.translator.wordwanderer.R

class TranslationAdapterModuleThree(private val context: Context, private val dataSource: List<ModuleThreeTranslation>) : BaseAdapter() {

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
        val rowView = convertView ?: inflater.inflate(R.layout.list_item_translation3, parent, false)

        val englishTextView = rowView.findViewById<TextView>(R.id.englishText)
        val translationTextView = rowView.findViewById<TextView>(R.id.textTranslation)

        val translation = getItem(position) as ModuleThreeTranslation

        englishTextView.text = translation.english
        translationTextView.text = translation.translationword

        return rowView
    }
}
