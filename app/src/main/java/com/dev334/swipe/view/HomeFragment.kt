package com.dev334.swipe.view

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dev334.swipe.R
import com.dev334.swipe.adapter.ProductAdapter
import com.dev334.swipe.databinding.FragmentHomeBinding
import com.dev334.swipe.model.Product
import com.dev334.swipe.viewmodel.HomeViewModel


class HomeFragment : Fragment() {

    private val sharedViewModel by activityViewModels<HomeViewModel>()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var products: ArrayList<Product>
    private lateinit var productAdapter: ProductAdapter
    private lateinit var loadingDialog: View
    private lateinit var loadingDialogInstance: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        (activity as MainActivity?)!!.showLoading()

        prepareRecyclerView()

        sharedViewModel.getProducts()!!.observe(viewLifecycleOwner, Observer { productList ->
            Log.i("FetchedProduct", "onCreate: Successful")
            (activity as MainActivity?)!!.dismissLoading()
            products = productList as ArrayList<Product>;
            productAdapter.setProductList(products)
        })

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //
            }
            override fun afterTextChanged(s: Editable) {
                showSearchProducts(s.toString())
            }
        })

        return fragmentBinding.root
    }

    private fun showSearchProducts(query: String) {
        if(query.isEmpty()){
            productAdapter.setProductList(products)
            return;
        }
        Log.i("QueryDebugger", "showSearchProducts: $query")
        val filterProducts = ArrayList<Product>()
        for (item in products){
            if(item.product_name?.lowercase()?.contains(query.lowercase()) == true){
                Log.i("QueryDebugger", "showSearchProducts: ${item.product_name}")
                filterProducts.add(item)
            }
        }

        productAdapter.updateToFilteredList(filterProducts)
    }

    private fun prepareRecyclerView() {
        productAdapter = ProductAdapter()
        binding.recyclerViewProducts.apply {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
            addItemDecoration(SpacesItemDecoration(10))
        }
    }

    private fun showLoading() {
        //Inflate the dialog as custom view
        loadingDialog = LayoutInflater.from(activity).inflate(R.layout.loading_layout, null)

        //AlertDialogBuilder
        val loadingDialogBuilder = AlertDialog.Builder(activity).setView(loadingDialog)

        //show dialog
        loadingDialogInstance = loadingDialogBuilder.show()
        loadingDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun dismissLoading(){
        loadingDialogInstance.dismiss()
    }
}

