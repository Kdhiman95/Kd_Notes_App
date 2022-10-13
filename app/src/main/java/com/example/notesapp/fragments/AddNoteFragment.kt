package com.example.notesapp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.NoteApplication
import com.example.notesapp.R
import com.example.notesapp.modal.room_database.Note
import com.example.notesapp.view_modal.NoteViewModel
import com.example.notesapp.view_modal.NoteViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddNoteFragment : Fragment() {

	private lateinit var noteViewModel: NoteViewModel
	private lateinit var inputTitle: AutoCompleteTextView
	private lateinit var inputNote: EditText
	private lateinit var addBtn: Button
	private lateinit var dateShow: TextView

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_add_note, container, false)
		inputTitle = view.findViewById(R.id.inputTitle)
		inputNote = view.findViewById(R.id.inputNote)
		addBtn = view.findViewById(R.id.addBtn)
		dateShow = view.findViewById(R.id.dateShow)

		val activity = activity as FragmentActivity
		val repository = (activity.application as NoteApplication).noteRepository

		noteViewModel =
			ViewModelProvider(this, NoteViewModelFactory(repository))[NoteViewModel::class.java]

		noteViewModel.noteList.observe(viewLifecycleOwner) {
			var titleList: Array<out String> = arrayOf()
			for (title in noteViewModel.noteList.value!!) {
				titleList = append(titleList,title.title)
			}
			val suggestion: Array<out String> = titleList
			val autoAdapter =
				ArrayAdapter(activity.applicationContext, android.R.layout.simple_list_item_1, suggestion)
			inputTitle.setAdapter(autoAdapter)
		}

		if (arguments != null) {
			val position = requireArguments().getInt("Position")
			inputTitle.setText(requireArguments().getString("Title"))
			inputNote.setText(requireArguments().getString("Note"))
			val lastEdited = "Last Edited: " + requireArguments().getString("CurDate")
			dateShow.text = lastEdited
			dateShow.visibility = View.VISIBLE

			addBtn.setOnClickListener {
				if (inputTitle.text.isNotBlank() || inputNote.text.isNotBlank()) {
					val title = inputTitle.text.trim().toString()
					val note = inputNote.text.trim().toString()
					val curDate = getCurDate()
					noteViewModel.updateNote(noteViewModel.noteList.value?.get(position)?.id!!,
						title,
						note,
						curDate)
					parentFragmentManager.popBackStack()
					parentFragmentManager.beginTransaction().setCustomAnimations(
						R.anim.slide_in,
						R.anim.fade_out,
						R.anim.fade_in,
						R.anim.slide_out
					)
						.replace(R.id.container, NotesShowFragment())
						.commit()
					val imm: InputMethodManager? =
						context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
					imm?.hideSoftInputFromWindow(view.windowToken, 0)
				} else {
					inputTitle.error = "Enter title"
					inputNote.error = "Enter note"
				}
			}
		} else {
			dateShow.visibility = View.GONE
			addBtn.setOnClickListener {
				if (inputTitle.text.isNotBlank() || inputNote.text.isNotBlank()) {
					val title = inputTitle.text.trim().toString()
					val note = inputNote.text.trim().toString()
					val curDate = getCurDate()
					noteViewModel.insertNote(Note(0, title, note, curDate))
					parentFragmentManager.popBackStack()
					parentFragmentManager.beginTransaction().setCustomAnimations(
						R.anim.slide_in,
						R.anim.fade_out,
						R.anim.fade_in,
						R.anim.slide_out
					)
						.replace(R.id.container, NotesShowFragment())
						.commit()
					val imm: InputMethodManager? =
						context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
					imm?.hideSoftInputFromWindow(view.windowToken, 0)
				} else {
					inputTitle.error = "Enter title"
					inputNote.error = "Enter note"
				}
			}
		}

		return view
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