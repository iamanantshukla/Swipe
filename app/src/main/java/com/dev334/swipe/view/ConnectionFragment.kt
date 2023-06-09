package com.dev334.swipe.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev334.swipe.databinding.FragmentConnectionBinding

class ConnectionFragment : Fragment() {
    private lateinit var binding: FragmentConnectionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentConnectionBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        (activity as MainActivity?)?.dismissLoading()
        binding.buttonRetry.setOnClickListener{
            if((activity as MainActivity?)!!.isNetworkAvailable(context)){
                (activity as MainActivity?)?.openHomeFragment()
            }
        }

        return binding.root
    }
}