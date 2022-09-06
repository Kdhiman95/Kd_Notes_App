package com.example.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.MainActivity
import com.example.notesapp.NotesAdapter
import com.example.notesapp.R

class NotesShowFragment : Fragment() {

    private lateinit var recV: RecyclerView
    private lateinit var addNoteBtn: Button
    private lateinit var data: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notes_show, container, false)

        parentFragmentManager.popBackStack()

        data = activity as MainActivity

        val list = data.data.getData()

        recV = view.findViewById(R.id.notesRecV)
        val adapter = NotesAdapter(list)

        addNoteBtn = view.findViewById(R.id.addNoteBtn)
        addNoteBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.container, AddNoteFragment())
                .addToBackStack(null).commit()
        }

        recV.adapter = adapter
        recV.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)


        return view
    }
}