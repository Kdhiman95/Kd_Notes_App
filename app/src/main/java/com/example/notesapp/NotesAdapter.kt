package com.example.notesapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Constant.Companion.noteList
import com.example.notesapp.fragments.AddNoteFragment

class NotesAdapter(private val notes: ArrayList<Bundle>, private val context: Context) :
	RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
	private val dataPref = Data(context)

	inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
		val title: TextView
		val note: TextView
		private val noteView: CardView

		init {
			title = item.findViewById(R.id.title)
			note = item.findViewById(R.id.description)
			noteView = item.findViewById(R.id.noteView)

			noteView.setOnClickListener {
				val activity = context as FragmentActivity
				val bundle = Bundle()
				bundle.putInt("Position", adapterPosition)
				val updateNote = AddNoteFragment()
				updateNote.arguments = bundle
				activity.supportFragmentManager.beginTransaction()
					.replace(R.id.container, updateNote).addToBackStack(null).commit()
			}

			noteView.setOnLongClickListener {
				val deleteDialog = AlertDialog.Builder(context)
				deleteDialog.setTitle("Delete")
					.setMessage("are you sure?")
					.setPositiveButton("Yes") { _, _ ->
//						Log.d("pos", position.toString())
						noteList.removeAt(adapterPosition)
						dataPref.saveData()
						notifyItemRemoved(adapterPosition)
					}
					.setNegativeButton("no") { _, _ -> }
					.show()
				true
			}
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