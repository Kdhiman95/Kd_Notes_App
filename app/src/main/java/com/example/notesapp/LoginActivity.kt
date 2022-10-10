package com.example.notesapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

	private lateinit var inputUserName: EditText
	private lateinit var inputUserPswd: EditText
	private lateinit var loginBtn: Button
	private lateinit var usernameLayout: TextInputLayout
	private lateinit var userPswdLayout: TextInputLayout

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		val pref = getSharedPreferences("Notes", MODE_PRIVATE)
		val editor = pref.edit()

		inputUserName = findViewById(R.id.inputUserName)
		inputUserPswd = findViewById(R.id.inputUserPswd)
		loginBtn = findViewById(R.id.loginBtn)
		usernameLayout = findViewById(R.id.usernameLayout)
		userPswdLayout = findViewById(R.id.userPswdLayout)

		loginBtn.setOnClickListener {
			if (inputUserName.text.isNotBlank() && inputUserPswd.text.isNotBlank()) {
				if (inputUserName.text.toString() == pref.getString("Username",
						null) && inputUserPswd.text.toString() == pref.getString("Password", null)
				) {
					editor.putBoolean("LoggedIn", true)
					editor.apply()
					startActivity(Intent(this, MainActivity::class.java))
					finish()
				} else if (inputUserName.text.toString() != pref.getString("Username", null))
					usernameLayout.error = "Enter valid username"
				else
					userPswdLayout.error = "Enter valid Password"

			} else if (inputUserName.text.isBlank())
				usernameLayout.error = "Enter username"
			else
				userPswdLayout.error = "Enter password"
			val imm: InputMethodManager =
				(this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?)!!
			imm.hideSoftInputFromWindow(it.windowToken, 0)
		}

	}
}