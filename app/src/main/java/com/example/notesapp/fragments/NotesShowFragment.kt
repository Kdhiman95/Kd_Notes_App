package com.example.notesapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.Data
import com.example.notesapp.NotesAdapter
import com.example.notesapp.R

class NotesShowFragment : Fragment() {

	private lateinit var recV: RecyclerView
	private lateinit var addNoteBtn: Button
	private lateinit var dataPref: Data

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_notes_show, container, false)


		dataPref = Data(requireContext())

		recV = view.findViewById(R.id.notesRecV)

		val list = dataPref.getData()
		val adapter = NotesAdapter(list, requireContext())
		recV.adapter = adapter
		recV.layoutManager =
			GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL, false)


		addNoteBtn = view.findViewById(R.id.addNoteBtn)
		addNoteBtn.setOnClickListener {
			parentFragmentManager.beginTransaction().replace(R.id.container, AddNoteFragment())
				.addToBackStack(null).commit()
		}

		Log.d("JKJK", "onCreateView: Helo")
		return view
	}
}