package com.john.itoo.routinecheckks

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

class SplashScreen : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Timber.d("Splash Intent")
        Timber.d((intent.toString()))

        if (intent.extras != null) {
            val newIntent = Intent(this, MainActivity::class.java)
            Timber.d("Check Routine" + intent.extras.get("Routine"))
            newIntent.putExtras(intent)
            startActivity(newIntent)
        } else {
            Handler().postDelayed({

                startActivity(Intent(this, MainActivity::class.java))

                finish()
            }, SPLASH_TIME_OUT)
        }
    }
}