package com.example.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.notesapp.Data
import com.example.notesapp.R
import com.example.notesapp.noteList

class AddNoteFragment : Fragment() {

	private lateinit var inputTitle: EditText
	private lateinit var inputNote: EditText
	private lateinit var addBtn: Button
	private lateinit var dataPref: Data

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

		if (arguments != null) {
			val adapterPosition = requireArguments().getInt("Position")
			addBtn.setText(R.string.update)
			inputTitle.setText(noteList[adapterPosition].getString("Title"))
			inputNote.setText((noteList[adapterPosition].getString("Note")))
			addBtn.setOnClickListener {
				if (inputTitle.text.isNotBlank() || inputNote.text.isNotBlank()) {
					val bundle = Bundle()
					val title = inputTitle.text.toString()
					bundle.putString("Title", title.trim())
					val note = inputNote.text.toString()
					bundle.putString("Note", note.trim())
					dataPref.updateData(bundle, adapterPosition)
					val showFragment = NotesShowFragment()
					showFragment.arguments?.putBundle("Bundle", bundle)
					parentFragmentManager.popBackStack()
					parentFragmentManager.beginTransaction().replace(R.id.container, showFragment)
						.commit()
				} else {
					inputTitle.error = "Enter title"
					inputNote.error = "Enter note"
				}
			}
		} else {
			addBtn.setOnClickListener {
				if (inputTitle.text.isNotBlank() || inputNote.text.isNotBlank()) {
					val bundle = Bundle()
					val title = inputTitle.text.toString()
					bundle.putString("Title", title.trim())
					val note = inputNote.text.toString()
					bundle.putString("Note", note.trim())
					dataPref.setData(bundle)
					val showFragment = NotesShowFragment()
					showFragment.arguments?.putBundle("Bundle", bundle)
					parentFragmentManager.popBackStack()
					parentFragmentManager.beginTransaction().replace(R.id.container, showFragment)
						.commit()
				} else {
					inputTitle.error = "Enter title"
					inputNote.error = "Enter note"
				}
			}
		}
		return view
	}
}