package com.example.notesapp.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notesapp.R
import com.example.notesapp.databinding.FragmentLoginScreenBinding

class LoginScreenFragment : Fragment() {

	private lateinit var imm: InputMethodManager

	private var _binding: FragmentLoginScreenBinding? = null
	private val binding get() = _binding!!


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		// Inflate the layout for this fragment
		_binding = FragmentLoginScreenBinding.inflate(inflater, container, false)

		imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

		val pref = requireContext().getSharedPreferences("Notes", AppCompatActivity.MODE_PRIVATE)
		val editor = pref.edit()

		val username = pref.getString("Username", null)
		val pswd = pref.getString("Password", null)

		binding.loginBtn.setOnClickListener {
			if (binding.inputUserName.text!!.isNotBlank() && binding.inputUserPswd.text!!.isNotBlank()) {
				if (binding.inputUserName.text.toString() == username && binding.inputUserPswd.text.toString() == pswd) {
					editor.putBoolean("LoggedIn", true)
					editor.apply()

					findNavController().navigate(R.id.action_loginScreenFragment_to_noteShowFragment)

				} else if (binding.inputUserName.text.toString() != username)
					binding.usernameLayout.error = "Enter valid username"
				else
					binding.userPswdLayout.error = "Enter valid Password"

			} else if (binding.inputUserName.text!!.isBlank())
				binding.usernameLayout.error = "Enter username"
			else
				binding.userPswdLayout.error = "Enter password"

			imm.hideSoftInputFromWindow(it.windowToken, 0)
		}

		return binding.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
	}
}