package com.dev334.swipe.view

import android.content.Intent
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
import com.dev334.swipe.databinding.FragmentAddProductBinding
import com.dev334.swipe.model.PostProduct
import com.dev334.swipe.viewmodel.HomeViewModel
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part.Companion.createFormData
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


class AddProductFragment : Fragment() {

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

        binding.buttonClose.setOnClickListener{
            (activity as MainActivity?)!!.openHomeFragment()
        }

        binding.buttonDone.setOnClickListener{
            (activity as MainActivity?)!!.showLoading()
            validateAndPostProduct()
        }

        return binding.root
    }

    // validate details and call post product
    private fun validateAndPostProduct() {
        var price: Double? = binding.editProductPrice.text.toString().toDoubleOrNull()
        var tax: Double? = binding.editProductTax.text.toString().toDoubleOrNull()
        var name: String? = binding.editProductName.text.toString()
        var type: String? = binding.editProductType.text.toString()
        var price_string: String? = binding.editProductPrice.text.toString()
        var tax_string: String? = binding.editProductTax.text.toString()

        if(checkProductDetails(name, type, price, tax)){

            //converting values into request body
            var name: RequestBody? = name!!.toRequestBody("text/plain".toMediaType())
            var type: RequestBody? = type!!.toRequestBody("text/plain".toMediaType())
            var price: RequestBody? = price_string!!.toRequestBody("text/plain".toMediaType())
            var tax: RequestBody? = tax_string!!.toRequestBody("text/plain".toMediaType())
            var multipartBody: MultipartBody.Part? = null

            if(file!=null) {
                multipartBody = createFormData("files[]", file?.name, file!!.asRequestBody());
            }

            //Request body of product
            var postProduct = PostProduct(name,type,price,tax,multipartBody)

            //calling the share view model add product functionality
            val viewModel = getViewModel<HomeViewModel> ()
            viewModel.addProduct(postProduct)!!.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                Toast.makeText(context, it.message, 2).show();
                (activity as MainActivity?)!!.dismissLoading()
                if(it.success == true){
                    binding.editProductPrice.text.clear()
                    binding.editProductName.text.clear()
                    binding.editProductTax.text.clear()
                    binding.editProductType.text.clear()
                    file = null;
                }
            })
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(Intent.createChooser(galleryIntent,
            "Select Image From Gallery "),0)
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
                UCrop.of(
                    uri,
                    Uri.fromFile(File(context!!.cacheDir, "IMG_" + System.currentTimeMillis()))
                ).withAspectRatio(1F, 1F)
                    .start(context!!, this)
            }
        }
        else if (requestCode == UCrop.REQUEST_CROP){
            if (data != null){
                val uri = UCrop.getOutput(data)
                try {
                    // Creating file
                    try {
                        file = createImageFile()
                    } catch (ex: IOException) {
                        Log.d("ApiCallDebugging", "Error occurred while creating the file")
                    }
                    val inputStream: InputStream? =
                        activity!!.contentResolver.openInputStream(uri!!)
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

    private fun checkProductDetails(name: String?, type: String?, price: Double?, tax: Double?): Boolean {
        if(name.isNullOrEmpty()){
            binding.editProductName.error = "Required Field"
            return false;
        }else if(type.isNullOrEmpty()){
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