package com.example.notesapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Data.Companion.noteList
import com.example.notesapp.fragments.AddNoteFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class NotesAdapter(private var notes: ArrayList<Bundle>, private val context: Context) :
	RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
	private val dataPref = Data(context)

	fun filterList(filterList: ArrayList<Bundle>) {
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
				val activity = context as FragmentActivity
				val bundle = Bundle()
				bundle.putInt("Position", adapterPosition)
				val updateNote = AddNoteFragment()
				updateNote.arguments = bundle
				activity.supportFragmentManager.beginTransaction().setCustomAnimations(
					R.anim.slide_in,
					R.anim.fade_out,
					R.anim.fade_in,
					R.anim.slide_out
				)
					.replace(R.id.container, updateNote)
					.addToBackStack(null).commit()
			}

			noteView.setOnLongClickListener {
				val popupMenu = PopupMenu(context, noteView)
				popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
				popupMenu.setOnMenuItemClickListener { menuItem ->
					when (menuItem.title) {
						"Delete" -> {
							val deleteDialog = MaterialAlertDialogBuilder(context)
							deleteDialog.setTitle("Delete")
								.setMessage("are you sure?")
								.setPositiveButton("Yes") { _, _ ->
									noteList.removeAt(adapterPosition)
									dataPref.saveData()
									notifyItemRemoved(adapterPosition)
								}
								.setNegativeButton("No") { _, _ -> }
								.show()
						}
						"Edit" -> {
							val activity = context as FragmentActivity
							val bundle = Bundle()
							bundle.putInt("Position", adapterPosition)
							val updateNote = AddNoteFragment()
							updateNote.arguments = bundle
							activity.supportFragmentManager.beginTransaction().setCustomAnimations(
								R.anim.slide_in,
								R.anim.fade_out,
								R.anim.fade_in,
								R.anim.slide_out
							)
								.replace(R.id.container, updateNote)
								.addToBackStack(null).commit()
						}
					}
					true
				}
				popupMenu.show()
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