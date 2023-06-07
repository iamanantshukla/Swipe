package com.dev334.swipe.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.dev334.swipe.R
import com.dev334.swipe.databinding.FragmentHomeBinding
import com.dev334.swipe.model.Product
import com.dev334.swipe.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private val sharedViewModel by activityViewModels<HomeViewModel>()
    private var binding: FragmentHomeBinding? = null
    private var products: List<Product>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentHomeBinding.inflate(inflater, container, false)
        binding = fragmentBinding

        sharedViewModel.getProducts()!!.observe(viewLifecycleOwner, Observer { productList ->
            Log.i("FetchedProduct", "onCreate: Successful")
            products = productList;
        })

        return fragmentBinding.root
    }
}