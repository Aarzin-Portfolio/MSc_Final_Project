package com.leaf.birdtracking

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.ebizzApps.sevenminuteworkout.OnSup.CNF
import com.ebizzApps.sevenminuteworkout.OnSup.CSup
import com.leaf.birdtracking.OnSup.FormActivity
import com.leaf.birdtracking.database.AllTables
import kotlinx.android.synthetic.main.activity_login_screen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Pattern


class LoginScreen : AppCompatActivity(), View.OnClickListener {

    lateinit var allTables: AllTables

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_signup -> {
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
            R.id.button_login -> {
                validation()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        supportActionBar?.hide()
        login_signup.setOnClickListener(this)
        button_login.setOnClickListener(this)
        allTables = AllTables(this, null)

    }

    lateinit var cnf: CNF


    fun validation() {
        val pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val matcher = pattern.matcher(edit_email_login.text.toString().trim())
        if (!TextUtils.isEmpty(edit_email_login.text.toString()) && matcher.matches()) {
            if (!TextUtils.isEmpty(edit_password_login.text.toString())) {
                if (CSup.iC(this)) {
                    button_login.visibility = View.GONE
                    login_progress.visibility = View.VISIBLE
                    cnf = CSup.gN().create(CNF::class.java)
                    var success = cnf.doLogin(edit_email_login.text.toString(), edit_password_login.text.toString())
                    //success.enqueue(this@LoginScreen);
                    val status_login = allTables.login_verification(edit_email_login.text.toString(), edit_password_login.text.toString())
                    if (status_login) {
                        val SharedPreferences = getSharedPreferences("MainPref", 0);
                        val edit = SharedPreferences.edit();
                        val min = 20000
                        val max = 80000
                        val random = Random().nextInt(max - min + 1) + min
                        edit.putBoolean("login", true);
                        edit.putString("username", "DFBT" + random);
                        edit.apply()
                        edit.commit()
                        startActivity(Intent(this@LoginScreen, HomeScreen::class.java))

                    } else {
                        button_login.visibility = View.VISIBLE
                        login_progress.visibility = View.GONE
                        Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()

                    }


                } else {
                    Toast.makeText(this, "No Internet connection!!", Toast.LENGTH_LONG).show()

                }


            } else {
                edit_password_login.error = "Invalid User Email"

            }
        } else {
            edit_email_login.error = "Invalid User Email"

        }
    }
}
