package com.leaf.birdtracking.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.leaf.birdtracking.Models.Bird
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Base64
import com.bumptech.glide.Glide
import com.leaf.birdtracking.R


class Bird_Adapter(val context: Context, val birdList: ArrayList<Bird>) : RecyclerView.Adapter<Bird_Adapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): Bird_Adapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_bird_detail, parent, false))
    }


    override fun getItemCount(): Int {
        return birdList.size
    }

    override fun onBindViewHolder(holder: Bird_Adapter.MyViewHolder, pos: Int) {

        holder.bird_name.text = birdList.get(pos).bird_name
        holder.date.text = birdList.get(pos).date
//        holder.latitude.text = birdList.get(pos).latitude
//        holder.longitude.text = birdList.get(pos).longitude

        var category: String? = birdList.get(pos).category
        if (category == "" || category == null) {
            category = "rare species"
        }
        holder.bird_cat.text = category

        val desc: String? = birdList.get(pos).bird_desc
        if (TextUtils.isEmpty(desc) || desc.equals("") || desc == null) {
            holder.bird_desc.visibility = View.GONE
        } else {
            holder.bird_desc.visibility = View.VISIBLE
            holder.bird_desc.text = desc
        }

        val decodedByte = Base64.decode(birdList.get(pos).bird_image, Base64.DEFAULT)

        Glide.with(context)
                .load(decodedByte)
                .placeholder(R.drawable.empty_image)
                .into(holder.bird_image)
//        val bitmap = BitmapFactory
//                .decodeByteArray(decodedByte, 0, decodedByte.size)
//        if (bitmap != null) {
//            holder.bird_image.setImageBitmap(bitmap)
//        }

    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bird_name = itemView.findViewById(R.id.bird_name) as TextView
        //        val latitude = itemView.findViewById(R.id.latitude) as TextView
//        val longitude = itemView.findViewById(R.id.longitude) as TextView
        val bird_cat = itemView.findViewById(R.id.bird_cat) as TextView
        val bird_image = itemView.findViewById(R.id.bird_image) as ImageView
        val date = itemView.findViewById(R.id.date) as TextView
        val bird_desc = itemView.findViewById(R.id.bird_desc) as TextView
    }
}