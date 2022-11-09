package com.example.notesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.R
import com.example.notesapp.fragments.NoteShowFragment
import com.example.notesapp.modal.repository.NoteRepository
import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.modal.room_database.NoteDatabase
import com.example.notesapp.view_modal.NoteViewModel
import com.example.notesapp.view_modal.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class NotesAdapter(
	noteList: List<Note>,
	private val fragmentContext: NoteShowFragment,
	private val context: Context,
) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

	var notes: MutableList<Note> = noteList as MutableList<Note>

	@SuppressLint("NotifyDataSetChanged")
	fun filterList(filterList: MutableList<Note>) {
		notes = filterList
		notifyDataSetChanged()
	}

	inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
		val title: TextView
		val note: TextView
		private val noteView: CardView

		init {
			title = item.findViewById(R.id.title)
			note = item.findViewById(R.id.description)
			noteView = item.findViewById(R.id.noteView)

			noteView.setOnClickListener {
				updateNote()
			}
			noteView.setOnLongClickListener {
				val popupMenu = PopupMenu(context, noteView)
				popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
				popupMenu.setOnMenuItemClickListener { menuItem ->
					when (menuItem.title) {
						"Delete" -> {
							deleteNote()
						}
						"Edit" -> {
							updateNote()
						}
					}
					true
				}
				popupMenu.show()
				true
			}

		}

		private fun deleteNote() {
			val database = NoteDatabase.getDatabase(context)
			val repo = NoteRepository(database)
			val noteViewModel = ViewModelProvider(fragmentContext,
				NoteViewModelFactory(repo))[NoteViewModel::class.java]

			val deleteDialog = MaterialAlertDialogBuilder(context)
			deleteDialog.setTitle("Delete").setMessage("are you sure?")
				.setPositiveButton("Yes") { _, _ ->
					val note = notes[adapterPosition]
					noteViewModel.deleteNote(note)
					notes.removeAt(adapterPosition)
					notifyItemRemoved(adapterPosition)
				}.setNegativeButton("No") { _, _ -> }.show()
		}

		private fun updateNote() {
			val bundle = Bundle().also {
				it.putInt("Position", adapterPosition)
				it.putString("Title", notes[adapterPosition].title)
				it.putString("Note", notes[adapterPosition].note)
				it.putString("CurDate", notes[adapterPosition].curDate)
			}

			fragmentContext.findNavController()
				.navigate(R.id.action_noteShowFragment_to_addNoteFragment, bundle)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.notes, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.title.text = notes[position].title
		holder.note.text = notes[position].note
	}

	override fun getItemCount(): Int = notes.size
}