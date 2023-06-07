package com.dev334.swipe.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api : ApiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://app.getswipe.in/api/public/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }
}