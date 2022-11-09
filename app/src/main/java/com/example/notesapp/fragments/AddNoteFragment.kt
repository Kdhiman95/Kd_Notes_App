package com.example.notesapp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesapp.utils.NoteApplication
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentAddNoteBinding
import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.view_modal.NoteViewModel
import com.example.notesapp.view_modal.NoteViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddNoteFragment : Fragment() {

	private lateinit var noteViewModel: NoteViewModel
	private var _binding: FragmentAddNoteBinding? = null
	private val binding get() = _binding!!
	private lateinit var data: Note

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentAddNoteBinding.inflate(inflater, container, false)

		val repository =
			((activity as FragmentActivity).application as NoteApplication).noteRepository

		noteViewModel =
			ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]

		noteViewModel.noteList.observe(viewLifecycleOwner) {
			var titleList: Array<out String> = arrayOf()
			for (title in noteViewModel.noteList.value!!) {
				titleList = append(titleList, title.title)
			}
			val suggestion: Array<out String> = titleList
			val autoAdapter =
				ArrayAdapter(requireActivity().applicationContext,
					android.R.layout.simple_list_item_1,
					suggestion)
			binding.inputTitle.setAdapter(autoAdapter)
		}

		if (arguments != null) {
			val position = requireArguments().getInt("Position")
			binding.inputTitle.setText(requireArguments().getString("Title"))
			binding.inputNote.setText(requireArguments().getString("Note"))
			val lastEdited = "Last Edited: " + requireArguments().getString("CurDate")
			binding.dateShow.text = lastEdited
			binding.dateShow.visibility = View.VISIBLE

			binding.addBtn.setOnClickListener {
				val note = fetchNote()
				noteViewModel.updateNote(noteViewModel.noteList.value?.get(position)?.id!!,
					note.title,
					note.note,
					note.curDate)
				findNavController().navigate(R.id.action_addNoteFragment_to_noteShowFragment)

			}
		} else {
			binding.dateShow.visibility = View.GONE
			binding.addBtn.setOnClickListener {

				val note = fetchNote()
				noteViewModel.insertNote(note)

				findNavController().navigate(R.id.action_addNoteFragment_to_noteShowFragment)
			}
		}
		return binding.root
	}

	private fun fetchNote(): Note {
		if (binding.inputTitle.text.isNotBlank() || binding.inputNote.text.isNotBlank()) {
			val title = binding.inputTitle.text.trim().toString()
			val note = binding.inputNote.text.trim().toString()
			val curDate = getCurDate()
			data = Note(0,title,note,curDate)
			val imm: InputMethodManager? =
				context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
			imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
		} else {
			binding.inputTitle.error = "Enter title"
			binding.inputNote.error = "Enter note"
		}
		return data
	}

	private fun append(titleList: Array<out String>, title: String): Array<out String> {
		val list = titleList.toMutableList()
		list.add(title)
		return list.toTypedArray()
	}

	private fun getCurDate(): String {
		val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			LocalDateTime.now()
		} else {
			TODO("VERSION.SDK_INT<O")
		}
		val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
		val formattedDate = current.format(formatter)
		var count = 1
		var date = ""
		for (i in formattedDate) {
			date += if (i == ',') {
				if (count == 1) {
					count++
					continue
				} else {
					'\n'
				}
			} else {
				i
			}
		}
		return date
	}
}