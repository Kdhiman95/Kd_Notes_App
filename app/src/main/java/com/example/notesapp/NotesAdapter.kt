package com.example.notesapp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.fragments.AddNoteFragment
import com.example.notesapp.fragments.NotesShowFragment
import com.example.notesapp.modal.repository.NoteRepository
import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.modal.room_database.NoteDatabase
import com.example.notesapp.view_modal.NoteViewModel
import com.example.notesapp.view_modal.NoteViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class NotesAdapter(
	noteList: List<Note>,
	private val fragmentContext: NotesShowFragment,
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
				updateNote(it)
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
							updateNote(it)
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

		private fun updateNote(it: View) {
			val activity = it.context as AppCompatActivity
			val bundle = Bundle()
			bundle.putInt("Position", adapterPosition)
			bundle.putString("Title", notes[adapterPosition].title)
			bundle.putString("Note", notes[adapterPosition].note)
			bundle.putString("CurDate", notes[adapterPosition].curDate)
			val updateNote = AddNoteFragment()
			updateNote.arguments = bundle

			activity.supportFragmentManager.beginTransaction().setCustomAnimations(
				R.anim.slide_in,
				R.anim.fade_out,
				R.anim.fade_in,
				R.anim.slide_out
			).replace(R.id.container, updateNote).addToBackStack(null).commit()
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