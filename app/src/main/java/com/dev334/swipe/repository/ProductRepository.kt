package com.dev334.swipe.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.dev334.swipe.model.Product
import com.dev334.swipe.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ProductRepository {

    val productLiveData = MutableLiveData<List<Product>>()
    val TAG: String = "ApiCallDebugging"

    fun getProductsApiCall(): MutableLiveData<List<Product>> {
        RetrofitInstance.api.getProducts().enqueue(object: Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if(response.body()!=null){
                    productLiveData.value = response.body()!!
                }else{
                    Log.i(TAG, "onResponse: Response empty")
                    return;
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.i(TAG, "onFailure: "+t.message)
            }

        })
        return productLiveData
    }
}