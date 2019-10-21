package com.leaf.birdtracking.OnSup

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.leaf.birdtracking.Adapter.Bird_Adapter
import com.leaf.birdtracking.Models.Bird
import com.leaf.birdtracking.R
import com.leaf.birdtracking.database.AllTables
import kotlinx.android.synthetic.main.activity_birds_detail.*

class Birds_Detail : AppCompatActivity() {
    lateinit var allTables: AllTables
    var bird_list: ArrayList<Bird> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birds_detail)

        supportActionBar?.setTitle("Bird Detail")
        someTask(this@Birds_Detail).execute()
    }

    private fun setAdapter(list: ArrayList<Bird>?) {
        recyclerView.layoutManager = GridLayoutManager(this@Birds_Detail, 1)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.hasFixedSize()
        recyclerView.adapter = Bird_Adapter(this@Birds_Detail, list!!)

        progressBar.visibility = View.GONE
    }


    inner class someTask(val context: Context) : AsyncTask<ArrayList<Bird>, ArrayList<Bird>, ArrayList<Bird>>() {
        override fun onPreExecute() {
            super.onPreExecute()

            progressBar.visibility = View.VISIBLE
            allTables = AllTables(context, null)
        }

        override fun doInBackground(vararg params: ArrayList<Bird>?): ArrayList<Bird> {
            bird_list = allTables.getBirdData()
            return bird_list
        }


        override fun onPostExecute(list: ArrayList<Bird>?) {
            super.onPostExecute(list)
            val SharedPreferences = getSharedPreferences("MainPref", 0);
            val uName = SharedPreferences.getString("username", "DFBT20989");
            userName.text = uName
            setAdapter(list)
        }
    }
}
