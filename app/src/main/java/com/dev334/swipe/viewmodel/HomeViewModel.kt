package com.dev334.swipe.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dev334.swipe.model.Product
import com.dev334.swipe.repository.ProductRepository

class HomeViewModel: ViewModel() {
    private var productLiveData: MutableLiveData<List<Product>>? =null

    fun getProducts() : MutableLiveData<List<Product>>? {
        productLiveData = ProductRepository.getProductsApiCall()
        return productLiveData
    }
}