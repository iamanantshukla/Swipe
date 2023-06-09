package com.dev334.swipe.view

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.dev334.swipe.databinding.FragmentAddProductBinding
import com.dev334.swipe.model.PostProduct
import com.dev334.swipe.repository.ProductRepository
import com.dev334.swipe.retrofit.RetrofitInstance
import com.dev334.swipe.viewmodel.HomeViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AddProductFragment : Fragment() {

    private val sharedViewModel by activityViewModels<HomeViewModel>()
    private lateinit var binding: FragmentAddProductBinding
    private var file: File? = null;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddProductBinding.inflate(inflater, container, false)

        binding.buttonAddImage.setOnClickListener{
            openGallery()
        }

        binding.buttonDone.setOnClickListener{

            var price: Double? = binding.editProductPrice.text.toString().toDoubleOrNull()
            var tax: Double? = binding.editProductTax.text.toString().toDoubleOrNull()

            val product = PostProduct(
                binding.editProductName.text.toString(),
                binding.editProductType.text.toString(),
                binding.editProductPrice.text.toString(),
                binding.editProductTax.text.toString(),
                //arrayListOf(file)
            )

            if(checkProductDetails(product, price, tax)){
                Log.i("AddProductDebugging", "onCreateView: Success $product")
                val map: MutableMap<String, String> = mutableMapOf()
                var name: RequestBody = product.product_name!!.toRequestBody("text/plain".toMediaType())
                var type: RequestBody = product.product_type!!.toRequestBody("text/plain".toMediaType())
                var price: RequestBody = product.price!!.toRequestBody("text/plain".toMediaType())
                var tax: RequestBody = product.tax!!.toRequestBody("text/plain".toMediaType())
                var multipartBody: MultipartBody.Part? = null
                if(file!=null) {
                    var requestBody: RequestBody =
                        file?.path!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
                    multipartBody = createFormData("files[]", file?.name, file!!.asRequestBody());
                }
                //var multipartBody: MultipartBody.Part = createFormData("files[]","product.png", requestBody);
                Log.i("ApiDebuggingImage", "onCreateView: $multipartBody")
                addProduct(name,type,price,tax,multipartBody)
            }
        }

        return binding.root
    }

    private fun addProduct(name: RequestBody, type: RequestBody, price:RequestBody,tax: RequestBody, multipartBody: MultipartBody.Part?) {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Add Product")
        progressDialog.setMessage("Uploading, please wait")
        progressDialog.show()
        RetrofitInstance.api.addProducts(name, type, price, tax,multipartBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if(response.body()!=null){
                    Toast.makeText(context, "Product added successfully",2).show()
                    Log.i(ProductRepository.TAG, "onResponse: "+ response.body())
                }else{
                    Log.i(ProductRepository.TAG, "onResponse: Response Empty"+ response)
                }
                progressDialog.dismiss()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.i(ProductRepository.TAG, "onFailure: "+t.message)
                Toast.makeText(context, "Unable to reach our server",2).show()
                progressDialog.dismiss()
            }

        })
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(Intent.createChooser(galleryIntent,
            "Select Image From Gallery "),0)
    }

    private fun cropImages(uri: Uri) {
        /**set crop image*/
        try {
            val cropIntent = Intent("com.android.camera.action.CROP")
            cropIntent.setDataAndType(uri,"image/*")
            cropIntent.putExtra("crop",true)
            cropIntent.putExtra("aspectX",1)
            cropIntent.putExtra("aspectY",1)
            cropIntent.putExtra("scaleUpIfNeeded",true)
            cropIntent.putExtra("return-data",true)
            startActivityForResult(cropIntent,1)

        }catch (e: ActivityNotFoundException){
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = activity!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.absolutePath
        return image
    }

    @Throws(IOException::class)
    fun copyStream(input: InputStream, output: OutputStream) {
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (input.read(buffer).also { bytesRead = it } != -1) {
            output.write(buffer, 0, bytesRead)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0){
            if (data != null){
                val uri = data.data!!
                cropImages(uri)
            }
        }
        else if (requestCode == 1){
            if (data != null){
                val uri = data.data!!
                try {
                    // Creating file
                    try {
                        file = createImageFile()
                    } catch (ex: IOException) {
                        Log.d("ApiCallDebugging", "Error occurred while creating the file")
                    }
                    val inputStream: InputStream? =
                        activity!!.contentResolver.openInputStream(uri)
                    val fileOutputStream = FileOutputStream(file)
                    // Copying
                    if (inputStream != null) {
                        copyStream(inputStream, fileOutputStream)
                    }
                    fileOutputStream.close()
                    inputStream?.close()
                } catch (e: java.lang.Exception) {
                    Log.d("ApiCallDebugging", "onActivityResult: $e")
                }
                Log.i("AddProductDebugging", "onActivityResult: success")
            }
        }
    }

    private fun checkProductDetails(product: PostProduct, price: Double?, tax: Double?): Boolean {
        if(product.product_name.isNullOrEmpty()){
            binding.editProductName.error = "Required Field"
            return false;
        }else if(product.product_type.isNullOrEmpty()){
            binding.editProductType.error = "Required Field"
            return false;
        }else if(price == null){
            binding.editProductPrice.error = "Required Field"
            return false;
        }else if(tax == null){
            binding.editProductTax.error = "Required Field"
            return false;
        }
        return true;
    }
}