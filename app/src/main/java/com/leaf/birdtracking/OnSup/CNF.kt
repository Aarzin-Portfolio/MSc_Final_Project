package com.ebizzApps.sevenminuteworkout.OnSup

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface CNF {


    @FormUrlEncoded
    @POST("userinsert.php")
    abstract fun doRegister(@Field("name") name: String,
                            @Field("email") Email: String,
                            @Field("pass") Pass: String,
                            @Field("address") address: String,
                            @Field("postcode") postcode: String,
                            @Field("birthdate") birthdate: String
    ): Call<String>

    @FormUrlEncoded
    @POST("userlogin.php")
    abstract fun doLogin(@Field("email") email: String, @Field("password") password: String): Call<String>

    @FormUrlEncoded
    @POST("birdinfo.php")
    abstract fun birdInfo(@Field("date") date: String,
                          @Field("username") username: String,
                          @Field("latlong") latlong: String,
                          @Field("birdname") birdname: String,
                          @Field("category_bird") category_bird: String,
                          @Field("image") image: String,
                          @Field("image_name") image_name: String): Call<String>


}