package com.example.notesapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.notesapp.MainActivity
import com.example.notesapp.R

class AddNoteFragment : Fragment() {

    private lateinit var inputTitle: EditText
    private lateinit var inputNote: EditText
    private lateinit var addBtn: Button
    private lateinit var data: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)

        inputTitle = view.findViewById(R.id.inputTitle)
        inputNote = view.findViewById(R.id.inputNote)
        addBtn = view.findViewById(R.id.addBtn)

        data = activity as MainActivity

        addBtn.setOnClickListener {
            val title = inputTitle.text.toString()
            val note = inputNote.text.toString()
            val bundle = Bundle()
            bundle.putString("Title", title)
            bundle.putString("Note", note)
            data.data.setData(bundle)
            val showFragment = NotesShowFragment()
            showFragment.arguments?.putBundle("Bundle", bundle)
            parentFragmentManager.beginTransaction().replace(R.id.container, showFragment).commit()
        }

        return view
    }
}