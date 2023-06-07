package com.dev334.swipe.retrofit

import com.dev334.swipe.model.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {

    @GET("get")
    fun getProducts() : Call<List<Product>>

    @POST("/add")
    fun addProducts()

}