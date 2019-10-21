package com.leaf.birdtracking

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.ebizzApps.sevenminuteworkout.OnSup.CNF
import com.ebizzApps.sevenminuteworkout.OnSup.CSup
import com.ebizzApps.sevenminuteworkout.OnSup.CSup.Companion.iC
import com.leaf.birdtracking.database.AllTables
import kotlinx.android.synthetic.main.activity_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.xml.datatype.DatatypeConstants.MONTHS

class RegistrationActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener {

    lateinit var allTables: AllTables

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            datePick()

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.register_button -> {
                validation();
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        supportActionBar?.setTitle("Registration")

        register_button.setOnClickListener(this)
        edit_birthdate.setOnFocusChangeListener(this)
        allTables = AllTables(this, null)

    }


    fun datePick() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            var cal = Calendar.getInstance()
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd-MMM-yyyy"
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            var oo = sdf.format(cal?.time)
            edit_birthdate.setText(oo)
        }, year, month, day)

        dpd.show()
    }

    lateinit var cnf: CNF

    fun validation() {


        val pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")

        if (!TextUtils.isEmpty(edit_name.text.toString())) {

            val matcher = pattern.matcher(edit_email.text.toString().trim())
            if (!TextUtils.isEmpty(edit_email.text.toString()) && matcher.matches()) {
                if (!TextUtils.isEmpty(edit_password.text.toString())) {
                    if (edit_password.text.toString().equals(edit_password_c.text.toString())) {
                        if (!TextUtils.isEmpty(edit_birthdate.text.toString())) {
                            if (!TextUtils.isEmpty(edit_address.text.toString())) {
                                if (!TextUtils.isEmpty(edit_postcode.text.toString())) {


                                    if (iC(this)) {
                                        register_button.visibility = View.GONE
                                        registration_progress.visibility = View.VISIBLE
                                        cnf = CSup.gN().create(CNF::class.java)
                                        var success = cnf.doRegister(
                                                edit_name.text.toString(),
                                                edit_email.text.toString(),
                                                edit_password_c.text.toString(),
                                                edit_address.text.toString(),
                                                edit_postcode.text.toString(),
                                                edit_birthdate.text.toString())

                                        val status = allTables.UserInsert(edit_name.text.toString()
                                                , edit_email.text.toString(), edit_password_c.text.toString(),
                                                edit_address.text.toString(), edit_postcode.text.toString(),
                                                edit_birthdate.text.toString())

                                        if (status > 0) {
                                            Toast.makeText(this, "Congratulations,Registration successfully", Toast.LENGTH_LONG).show()
                                            startActivity(Intent(this@RegistrationActivity, LoginScreen::class.java))
                                        } else {
                                            register_button.visibility = View.VISIBLE
                                            registration_progress.visibility = View.GONE
                                            Toast.makeText(this, "Something went wrong please try again later!!", Toast.LENGTH_LONG).show()

                                        }
                                        // success.enqueue(this)


                                    } else {
                                        Toast.makeText(this, "No Internet connection!!", Toast.LENGTH_LONG).show()
                                    }


                                    // API CALLLLLL


                                } else {
                                    edit_address.error = "Invalid postcode"
                                }
                            } else {
                                edit_address.error = "Invalid Address"
                            }
                        } else {
                            edit_birthdate.error = "Invalid Birth Date"
                        }
                    } else {
                        edit_password.error = "both password should be match"
                        edit_password.setText("")
                        edit_password_c.setText("")
                    }
                } else {
                    edit_password.error = "Invalid Password"
                }
            } else {
                edit_email.error = "Invalid User Email"
            }
        } else {
            edit_name.error = "Invalid User Name"
        }

    }
}
