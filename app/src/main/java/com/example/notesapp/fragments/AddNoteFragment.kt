package com.example.notesapp.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.notesapp.Data
import com.example.notesapp.Data.Companion.noteList
import com.example.notesapp.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddNoteFragment : Fragment() {

	private lateinit var inputTitle: EditText
	private lateinit var inputNote: EditText
	private lateinit var addBtn: Button
	private lateinit var dataPref: Data
	private lateinit var dateShow: TextView

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_add_note, container, false)
		dataPref = Data(requireContext())
		inputTitle = view.findViewById(R.id.inputTitle)
		inputNote = view.findViewById(R.id.inputNote)
		addBtn = view.findViewById(R.id.addBtn)
		dateShow = view.findViewById(R.id.dateShow)

		if (arguments != null) {
			val adapterPosition = requireArguments().getInt("Position")
			addBtn.setText(R.string.update)
			inputTitle.setText(noteList[adapterPosition].getString("Title"))
			inputNote.setText((noteList[adapterPosition].getString("Note")))
			dateShow.visibility = View.VISIBLE
			val lastEdited = "Last Edited: " + noteList[adapterPosition].getString("CurDate")
			dateShow.text = lastEdited
			addBtn.setOnClickListener {
				if (inputTitle.text.isNotBlank() || inputNote.text.isNotBlank()) {
					val bundle = Bundle()
					val title = inputTitle.text.toString()
					bundle.putString("Title", title.trim())
					val note = inputNote.text.toString()
					bundle.putString("Note", note.trim())
					val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
						LocalDateTime.now()
					} else {
						TODO("VERSION.SDK_INT < O")
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
					bundle.putString("CurDate", date)
					dataPref.updateData(bundle, adapterPosition)
					val showFragment = NotesShowFragment()
					showFragment.arguments?.putBundle("Bundle", bundle)
					parentFragmentManager.popBackStack()
					parentFragmentManager.beginTransaction().setCustomAnimations(
						R.anim.slide_in,
						R.anim.fade_out,
						R.anim.fade_in,
						R.anim.slide_out
					)
						.replace(R.id.container, showFragment)
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
					val bundle = Bundle()
					val title = inputTitle.text.toString()
					bundle.putString("Title", title.trim())
					val note = inputNote.text.toString()
					bundle.putString("Note", note.trim())
					val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
						LocalDateTime.now()
					} else {
						TODO("VERSION.SDK_INT < O")
					}
					val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
					val formattedDate = current.format(formatter)
					var date = ""
					var count = 1
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
					bundle.putString("CurDate", date)
					dataPref.setData(bundle)
					val showFragment = NotesShowFragment()
					showFragment.arguments?.putBundle("Bundle", bundle)
					parentFragmentManager.popBackStack()
					parentFragmentManager.beginTransaction().setCustomAnimations(
						R.anim.slide_in,
						R.anim.fade_out,
						R.anim.fade_in,
						R.anim.slide_out
					)
						.replace(R.id.container, showFragment)
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
}