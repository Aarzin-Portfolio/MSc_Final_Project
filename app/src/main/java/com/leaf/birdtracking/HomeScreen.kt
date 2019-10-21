package com.leaf.birdtracking

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.leaf.birdtracking.OnSup.FormActivity
import com.leaf.birdtracking.OnSup.Birds_Detail
import com.leaf.birdtracking.database.AllTables
import kotlinx.android.synthetic.main.activity_home_screen.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.leaf.birdtracking.OnSup.BirdSerch
import java.io.ByteArrayOutputStream


class HomeScreen : AppCompatActivity(), View.OnClickListener {

    lateinit var allTables: AllTables
    var isInsert: Boolean? = false
    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.option1 -> {
                startActivity(Intent(this, Birds_Detail::class.java))
            }
            R.id.option2 -> {
                startActivity(Intent(this, FormActivity::class.java))
            }
            R.id.option3 -> {
                startActivity(Intent(this, BirdSerch::class.java))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
        supportActionBar?.hide()


        val sp = getSharedPreferences("InPref", 0);
        isInsert = sp.getBoolean("is_insert", false);

        if (isInsert == false) {
            home_progressbar.visibility = View.VISIBLE
            insertDummyData()
        } else {
            onClicks()
        }
    }

    private fun onClicks() {
        home_progressbar.visibility = View.GONE
        li_main.visibility = View.VISIBLE
        option1.setOnClickListener(this)
        option2.setOnClickListener(this)
        option3.setOnClickListener(this)
    }

    private fun insertDummyData() {
        allTables = AllTables(this, null)
        val SharedPreferences = getSharedPreferences("MainPref", 0);
        val userName = SharedPreferences.getString("username", "DFBT20989");

        val bitmapOrg_1 = BitmapFactory.decodeResource(resources, R.drawable.bird_1)
        val bao_1 = ByteArrayOutputStream()
        bitmapOrg_1.compress(Bitmap.CompressFormat.JPEG, 100, bao_1)
        val ba_1 = bao_1.toByteArray()
        val bit_1 = Base64.encodeToString(ba_1, Base64.DEFAULT)


        val bitmapOrg_2 = BitmapFactory.decodeResource(resources, R.drawable.bird_2)
        val bao_2 = ByteArrayOutputStream()
        bitmapOrg_2.compress(Bitmap.CompressFormat.JPEG, 100, bao_2)
        val ba_2 = bao_2.toByteArray()
        val bit_2 = Base64.encodeToString(ba_2, Base64.DEFAULT)


        val bitmapOrg_3 = BitmapFactory.decodeResource(resources, R.drawable.bird_3)
        val bao_3 = ByteArrayOutputStream()
        bitmapOrg_3.compress(Bitmap.CompressFormat.JPEG, 100, bao_3)
        val ba_3 = bao_3.toByteArray()
        val bit_3 = Base64.encodeToString(ba_3, Base64.DEFAULT)


        val bitmapOrg_4 = BitmapFactory.decodeResource(resources, R.drawable.bird_4)
        val bao_4 = ByteArrayOutputStream()
        bitmapOrg_4.compress(Bitmap.CompressFormat.JPEG, 100, bao_4)
        val ba_4 = bao_4.toByteArray()
        val bit_4 = Base64.encodeToString(ba_4, Base64.DEFAULT)

        val bitmapOrg_5 = BitmapFactory.decodeResource(resources, R.drawable.bird_5)
        val bao_5 = ByteArrayOutputStream()
        bitmapOrg_5.compress(Bitmap.CompressFormat.JPEG, 100, bao_5)
        val ba_5 = bao_5.toByteArray()
        val bit_5 = Base64.encodeToString(ba_5, Base64.DEFAULT)


        val success_1 = allTables.birdInsert(getString(R.string.date_1),
                userName,
                "" + getString(R.string.latitude_1),
                "" + getString(R.string.longitude_1),
                getString(R.string.bird_name_1),
                getString(R.string.category_1),
                bit_1,
                getString(R.string.desc_1),
                "")

        val success_2 = allTables.birdInsert(getString(R.string.date_2),
                userName,
                "" + getString(R.string.latitude_2),
                "" + getString(R.string.longitude_2),
                getString(R.string.bird_name_2),
                getString(R.string.category_2),
                bit_2,
                getString(R.string.desc_2),
                "")

        val success_3 = allTables.birdInsert(getString(R.string.date_3),
                userName,
                "" + getString(R.string.latitude_3),
                "" + getString(R.string.longitude_3),
                getString(R.string.bird_name_3),
                getString(R.string.category_3),
                bit_3,
                getString(R.string.desc_3),
                "")

        val success_4 = allTables.birdInsert(getString(R.string.date_4),
                userName,
                "" + getString(R.string.latitude_4),
                "" + getString(R.string.longitude_4),
                getString(R.string.bird_name_4),
                getString(R.string.category_4),
                bit_2,
                getString(R.string.desc_2),
                "")

        val success_5 = allTables.birdInsert(getString(R.string.date_5),
                userName,
                "" + getString(R.string.latitude_5),
                "" + getString(R.string.longitude_5),
                getString(R.string.bird_name_5),
                getString(R.string.category_5),
                bit_1,
                getString(R.string.desc_1),
                "")

        val success_6 = allTables.birdInsert(getString(R.string.date_6),
                userName,
                "" + getString(R.string.latitude_6),
                "" + getString(R.string.longitude_6),
                getString(R.string.bird_name_6),
                getString(R.string.category_6),
                bit_3,
                getString(R.string.desc_3),
                "")

        val success_7 = allTables.birdInsert(getString(R.string.date_7),
                userName,
                "" + getString(R.string.latitude_7),
                "" + getString(R.string.longitude_7),
                getString(R.string.bird_name_7),
                getString(R.string.category_7),
                bit_4,
                getString(R.string.desc_4),
                "")

        val success_8 = allTables.birdInsert(getString(R.string.date_8),
                userName,
                "" + getString(R.string.latitude_8),
                "" + getString(R.string.longitude_8),
                getString(R.string.bird_name_8),
                getString(R.string.category_8),
                bit_5,
                getString(R.string.desc_5),
                "")


        val success_9 = allTables.birdInsert(getString(R.string.date_9),
                userName,
                "" + getString(R.string.latitude_9),
                "" + getString(R.string.longitude_9),
                getString(R.string.bird_name_9),
                getString(R.string.category_9),
                bit_4,
                getString(R.string.desc_4),
                "")

        val success_10 = allTables.birdInsert(getString(R.string.date_10),
                userName,
                "" + getString(R.string.latitude_10),
                "" + getString(R.string.longitude_10),
                getString(R.string.bird_name_10),
                getString(R.string.category_10),
                bit_5,
                getString(R.string.desc_5),
                "")
        if (success_1 > 0 && success_2 > 0 && success_3 > 0 && success_4 > 0 && success_5 > 0 && success_6 > 0 && success_7 > 0) {
            isInsert = true
            onClicks()
        } else {
            isInsert = false
        }

        val sp = getSharedPreferences("InPref", 0);
        val edit = sp.edit();
        edit.putBoolean("is_insert", isInsert!!);
        edit.apply()
        edit.commit()
    }
}
