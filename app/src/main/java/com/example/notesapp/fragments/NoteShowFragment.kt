package com.example.notesapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.R
import com.example.notesapp.adapter.NotesAdapter
import com.example.notesapp.databinding.FragmentNoteShowBinding
import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.utils.NoteApplication
import com.example.notesapp.view_modal.NoteViewModel
import com.example.notesapp.view_modal.NoteViewModelFactory


class NoteShowFragment : Fragment(), MenuProvider {

	private lateinit var adapter: NotesAdapter
	private lateinit var noteViewModel: NoteViewModel

	private var _binding: FragmentNoteShowBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentNoteShowBinding.inflate(inflater, container, false)

		binding.toolBar.inflateMenu(R.menu.main_activity_menu)
		(activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
		(activity as AppCompatActivity).supportActionBar?.show()

		val menuHost: MenuHost = requireActivity()
		menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)

		val repo = ((activity as FragmentActivity).application as NoteApplication).noteRepository

		noteViewModel =
			ViewModelProvider(this, NoteViewModelFactory(repo))[NoteViewModel::class.java]

		binding.notesRecV.layoutManager =
			StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

		noteViewModel.noteList.observe(viewLifecycleOwner) {
			adapter = NotesAdapter(noteViewModel.noteList.value!!, this, requireContext())
			binding.notesRecV.adapter = adapter
		}

		binding.addNoteBtn.setOnClickListener {
			findNavController().navigate(R.id.action_noteShowFragment_to_addNoteFragment)
		}

		return binding.root
	}

	override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
		menuInflater.inflate(R.menu.main_activity_menu, menu)
	}

	override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
		return when (menuItem.itemId) {
			R.id.search -> {
				val search = menuItem.actionView as SearchView
				val txt: EditText = search.findViewById(androidx.appcompat.R.id.search_src_text)
				txt.hint = "Search"
				txt.setHintTextColor(Color.WHITE)
				txt.setTextColor(Color.WHITE)

				search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
					override fun onQueryTextSubmit(query: String): Boolean {
						filter(query)
						return false
					}

					override fun onQueryTextChange(newText: String): Boolean {
						filter(newText)
						return false
					}
				})
				true
			}
			R.id.logout -> {
				val pref =
					requireContext().getSharedPreferences("Notes", AppCompatActivity.MODE_PRIVATE)
				val editor = pref.edit()
				editor.putBoolean("LoggedIn", false)
				editor.apply()
				findNavController().navigate(R.id.action_noteShowFragment_to_loginScreenFragment)
				Toast.makeText(requireContext(), "LOGOUT", Toast.LENGTH_SHORT).show()
				true
			}
			else -> false
		}
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