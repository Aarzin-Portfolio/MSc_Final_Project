package com.leaf.birdtracking.Adapter

import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.leaf.birdtracking.R
import com.leaf.birdtracking.database.AllTables

class SearchAdapter(context: Context, c: Cursor, flags: Int) : CursorAdapter(context, c, flags) {

    private val mInflater: LayoutInflater

    init {
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        val view = mInflater.inflate(R.layout.cell_search, parent, false)
        val holder = MyViewHolder()
        holder.bird_name = view.findViewById<View>(R.id.bird_name) as TextView
        holder.date = view.findViewById<View>(R.id.date) as TextView
        holder.latitude = view.findViewById<View>(R.id.latitude) as TextView
        holder.longitude = view.findViewById<View>(R.id.longitude) as TextView
        holder.bird_cat = view.findViewById<View>(R.id.bird_cat) as TextView
        holder.bird_image = view.findViewById<View>(R.id.bird_image) as ImageView
        view.tag = holder
        return view
    }

    override fun bindView(view: View, context: Context, cursor: Cursor) {

        val holder = view.tag as MyViewHolder
        holder.date!!.text = cursor.getString(cursor.getColumnIndexOrThrow(AllTables.DATE))
        holder.bird_name!!.text = cursor.getString(cursor.getColumnIndexOrThrow(AllTables.BIRDNAME))
        holder.latitude!!.text = cursor.getString(cursor.getColumnIndexOrThrow(AllTables.LATITUDE))
        holder.longitude!!.text = cursor.getString(cursor.getColumnIndexOrThrow(AllTables.LONGITUDE))

        var category: String? = cursor.getString(cursor.getColumnIndexOrThrow(AllTables.BIRDCATEGORY))
        if (category == "" || category == null) {
            category = "rare species"
        }
        holder.bird_cat!!.text = category

        val decodedByte = Base64.decode(cursor.getString(cursor.getColumnIndexOrThrow(AllTables.IMAGE)), Base64.DEFAULT)
//        Glide.with(context)
//                .load(decodedByte)
//                .placeholder(R.drawable.empty_image)
//                .into(holder.bird_image!!)

        val bitmap = BitmapFactory
                .decodeByteArray(decodedByte, 0, decodedByte.size)
        if (bitmap != null) {
            holder.bird_image!!.setImageBitmap(bitmap)
        }
    }

    internal class MyViewHolder {
        var bird_name: TextView? = null
        var date: TextView? = null
        var longitude: TextView? = null
        var latitude: TextView? = null
        var bird_cat: TextView? = null
        var bird_image: ImageView? = null
    }
}
