package com.example.notesapp.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R

class StartScreenFragment : Fragment() {

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View? {
		// Inflate the layout for this fragment
		val view = inflater.inflate(R.layout.fragment_start_screen, container, false)

		val pref = requireContext().getSharedPreferences("Notes", AppCompatActivity.MODE_PRIVATE)
		val editor = pref.edit()
		editor.putString("Username", "Admin")
		editor.putString("Password", "123")
		editor.apply()


		Handler(Looper.myLooper()!!).postDelayed({
			val loggedIn = pref.getBoolean("LoggedIn", false)
			if (!loggedIn) {
				findNavController().navigate(R.id.action_startScreenFragment_to_loginScreenFragment)
			} else {
				findNavController().navigate(R.id.action_startScreenFragment_to_noteShowFragment)
			}
		}, 500)

		return view
	}
}