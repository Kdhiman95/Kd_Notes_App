package com.example.notesapp.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.LoginActivity
import com.example.notesapp.NoteApplication
import com.example.notesapp.NotesAdapter
import com.example.notesapp.R
import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.view_modal.NoteViewModel
import com.example.notesapp.view_modal.NoteViewModelFactory


class NotesShowFragment : Fragment() {

	private lateinit var recV: RecyclerView
	private lateinit var addNoteBtn: Button
	private lateinit var toolbar: androidx.appcompat.widget.Toolbar
	private lateinit var noteViewModel: NoteViewModel
	private lateinit var adapter: NotesAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_notes_show, container, false)

		toolbar = view.findViewById(R.id.toolBar)
		setHasOptionsMenu(true)
		toolbar.inflateMenu(R.menu.main_activity_menu)
		(activity as AppCompatActivity).setSupportActionBar(toolbar)
		(activity as AppCompatActivity).supportActionBar?.show()

		val activity = activity as FragmentActivity

		val repository = (activity.application as NoteApplication).noteRepository

		noteViewModel =
			ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]

		recV = view.findViewById(R.id.notesRecV)
		recV.layoutManager =
			StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

		noteViewModel.noteList.observe(viewLifecycleOwner) {
			adapter = NotesAdapter(noteViewModel.noteList.value!!, this, requireContext())
			recV.adapter = adapter
		}

		addNoteBtn = view.findViewById(R.id.addNoteBtn)
		addNoteBtn.setOnClickListener {
			parentFragmentManager.beginTransaction().setCustomAnimations(
				R.anim.slide_in,
				R.anim.fade_out,
				R.anim.fade_in,
				R.anim.slide_out
			)
				.replace(R.id.container, AddNoteFragment())
				.addToBackStack(null).commit()
		}

		return view
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.main_activity_menu, menu)
		val search = menu.findItem(R.id.search).actionView as SearchView
		search.queryHint = "Search"
		search.setBackgroundColor(Color.WHITE)
		search.setOnQueryTextListener(object :
			SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(text: String): Boolean {
				filter(text)
				return false
			}

			override fun onQueryTextChange(text: String): Boolean {
				filter(text)
				return false
			}
		})
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {

		when (item.itemId) {
			R.id.logout -> {
				val pref =
					requireContext().getSharedPreferences("Notes", AppCompatActivity.MODE_PRIVATE)
				val editor = pref.edit()
				editor.putBoolean("LoggedIn", false)
				editor.apply()
				val intent = Intent(requireContext(), LoginActivity::class.java)
				startActivity(intent)
				Toast.makeText(requireContext(), "LOGOUT", Toast.LENGTH_SHORT).show()
			}
		}
		return super.onOptionsItemSelected(item)
	}

	private fun filter(text: String) {
		val filterList: MutableList<Note> = mutableListOf()
		for (note in noteViewModel.noteList.value!!) {
			if (note.title.lowercase()
					.contains(text.lowercase()) ||
				note.note.lowercase()
					.contains(text.lowercase())
			) {
				filterList.add(note)
			}
		}
		adapter.filterList(filterList)
	}
}