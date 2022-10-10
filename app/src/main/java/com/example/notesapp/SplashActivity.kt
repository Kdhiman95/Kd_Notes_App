package com.example.notesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_splash)

		val pref = getSharedPreferences("Notes", MODE_PRIVATE)
		val editor = pref.edit()
		editor.putString("Username", "Admin")
		editor.putString("Password", "123")
		editor.apply()

		Handler(Looper.myLooper()!!).postDelayed({
			val loggedIn = pref.getBoolean("LoggedIn", false)
			if (loggedIn) {
				startActivity(Intent(this, MainActivity::class.java))
			} else {
				startActivity(Intent(this, LoginActivity::class.java))
			}
			finish()
		}, 500)

	}
}