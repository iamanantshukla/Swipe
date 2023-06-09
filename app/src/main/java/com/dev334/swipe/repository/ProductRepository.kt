package com.dev334.swipe.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dev334.swipe.model.ApiResponse
import com.dev334.swipe.model.PostProduct
import com.dev334.swipe.model.Product
import com.dev334.swipe.retrofit.RetrofitInstance
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository {

    val productLiveData = MutableLiveData<List<Product>>()
    val responseBody = MutableLiveData<ApiResponse>()
    val TAG: String = "ApiCallDebugging"

    fun getProductsApiCall(): MutableLiveData<List<Product>> {
        RetrofitInstance.api.getProducts().enqueue(object: Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if(response.body()!=null){
                    productLiveData.value = response.body()!!
                }else{
                    Log.i(TAG, "onResponse: Response empty"+ response.message())
                    return;
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.i(TAG, "onFailure: "+t.message)
            }

        })
        return productLiveData
    }

    fun postProductApiCall(postProduct: PostProduct): MutableLiveData<ApiResponse> {
        RetrofitInstance.api.addProducts(postProduct.name,
            postProduct.type, postProduct.price, postProduct.tax, postProduct.file).enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                if(response.body()!=null){
                    Log.i(ProductRepository.TAG, "onResponse: "+ response.body())
                    responseBody.value = response.body()
                }else{
                    Log.i(ProductRepository.TAG, "onResponse: Response Empty: $response")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.i(ProductRepository.TAG, "onFailure: "+t.message)
            }

        })
        return responseBody
    }

}