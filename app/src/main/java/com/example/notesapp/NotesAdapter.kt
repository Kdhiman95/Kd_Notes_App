package com.example.notesapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private val notes: ArrayList<Bundle>) :
    RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val title: TextView
        val note: TextView

        init {
            title = item.findViewById(R.id.title)
            note = item.findViewById(R.id.description)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notes, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = notes[position].getString("Title")
        holder.note.text = notes[position].getString("Note")
    }

    override fun getItemCount(): Int = notes.size
}