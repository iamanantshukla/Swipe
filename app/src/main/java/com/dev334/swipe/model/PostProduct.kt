package com.dev334.swipe.model

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody

data class PostProduct(
    val name: RequestBody? = null,
    val type: RequestBody? = null,
    val price: RequestBody? = null,
    val tax: RequestBody? = null,
    val file: MultipartBody.Part? = null
)