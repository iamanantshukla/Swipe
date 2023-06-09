package com.dev334.swipe.retrofit

import com.dev334.swipe.model.ApiResponse
import com.dev334.swipe.model.PostProduct
import com.dev334.swipe.model.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface ApiInterface {

    @Headers("Content-Type: application/json")
    @GET("get")
    fun getProducts() : Call<List<Product>>

    @Multipart
    @POST("add")
    fun addProducts(@Part("product_name") name: RequestBody,
                    @Part("product_type") type: RequestBody,
                    @Part("price") price: RequestBody,
                    @Part("tax") tax: RequestBody,
                    @Part file: MultipartBody.Part?) : Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @GET("add")
    fun addProducts() : Call<ResponseBody>

}