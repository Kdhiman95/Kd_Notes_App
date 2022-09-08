package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

	private lateinit var inputUserName: EditText
	private lateinit var inputUserPswd: EditText
	private lateinit var loginBtn: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		val pref = getSharedPreferences("Notes", MODE_PRIVATE)
		val editor = pref.edit()

		inputUserName = findViewById(R.id.inputUserName)
		inputUserPswd = findViewById(R.id.inputUserPswd)
		loginBtn = findViewById(R.id.loginBtn)

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
					inputUserName.error = "Enter valid username"
				else
					inputUserPswd.error = "Enter valid Password"

			} else if (inputUserName.text.isBlank())
				inputUserName.error = "Enter username"
			else
				inputUserPswd.error = "Enter password"
		}

	}
}