package com.dev334.swipe.module

import com.dev334.swipe.repository.ProductRepository
import com.dev334.swipe.retrofit.ApiInterface
import com.dev334.swipe.viewmodel.HomeViewModel
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://app.getswipe.in/api/public/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiInterface::class.java)
    }
    single<ProductRepository> {
        ProductRepository(get())
    }

    viewModel{
        HomeViewModel(get())
    }

}