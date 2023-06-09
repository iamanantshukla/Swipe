package com.dev334.swipe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev334.swipe.model.ApiResponse
import com.dev334.swipe.model.PostProduct
import com.dev334.swipe.model.Product
import com.dev334.swipe.repository.ProductRepository
import okhttp3.ResponseBody

class HomeViewModel: ViewModel() {
    private var productLiveData: MutableLiveData<List<Product>>? =null
    private var responseBody: MutableLiveData<ApiResponse>? = null

    fun getProducts() : MutableLiveData<List<Product>>? {
        productLiveData = ProductRepository.getProductsApiCall()
        return productLiveData
    }

    fun addProduct(postProduct: PostProduct) : MutableLiveData<ApiResponse>? {
        responseBody = ProductRepository.postProductApiCall(postProduct)
        return responseBody
    }
}