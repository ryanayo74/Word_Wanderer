package com.translator.wordwanderer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.translator.wordwanderer.model.ModuleFourTranslation
import com.translator.wordwanderer.R

class TranslationAdapterModuleFour(private val translations: List<ModuleFourTranslation>) : RecyclerView.Adapter<TranslationAdapterModuleFour.TranslationViewHolder>() {

    class TranslationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val englishTextView: TextView = view.findViewById(R.id.englishTextView)
        val translationTextView: TextView = view.findViewById(R.id.translationTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_translation4, parent, false)
        return TranslationViewHolder(view)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        val translation = translations[position]
        holder.englishTextView.text = translation.english
        holder.translationTextView.text = translation.translation
    }

    override fun getItemCount() = translations.size
}
