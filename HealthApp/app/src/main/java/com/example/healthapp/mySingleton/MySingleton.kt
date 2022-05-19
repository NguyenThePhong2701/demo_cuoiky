package com.example.healthapp.mySingleton

import com.example.healthapp.network.MyNetwork
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


var retrofit = Retrofit.Builder()
    .baseUrl("http://10.0.2.2:3000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object MySingleton {
    val network: MyNetwork by lazy {
        retrofit.create(MyNetwork::class.java)
    }
}