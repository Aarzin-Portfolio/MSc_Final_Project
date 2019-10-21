package com.ebizzApps.sevenminuteworkout.OnSup

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CSup {

    companion object {

        internal val ip = "192.168.0.5"
        val BASE_URL = "http://$ip/birdtracking/"

        fun iC(c: Context): Boolean {
            val cm = c.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }


        internal var gson = GsonBuilder()
                .setLenient()
                .create()

        var r: Retrofit? = null
        fun gN(): Retrofit {
            if (r == null) {
                r = Retrofit.Builder().baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build()

            }
            return r!!;
        }
    }

}