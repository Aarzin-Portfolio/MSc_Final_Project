package com.leaf.birdtracking

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.bumptech.glide.Glide
import com.leaf.birdtracking.OnSup.FormActivity
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed(Runnable {

            val SharedPreferences = getSharedPreferences("MainPref", 0);

            if (SharedPreferences.getBoolean("login", false)) {
                startActivity(Intent(this@SplashActivity, HomeScreen::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, LoginScreen::class.java))
            }

        }, 3000);

    }
}
