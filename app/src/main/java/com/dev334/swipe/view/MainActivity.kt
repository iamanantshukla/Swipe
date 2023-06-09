package com.dev334.swipe.view

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev334.swipe.R
import com.dev334.swipe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    private lateinit var loadingDialog: View
    private lateinit var loadingDialogInstance: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener{
            replaceFragment(AddProductFragment())
            binding.floatingActionButton.visibility = View.GONE
        }

    }

    private fun replaceFragment(fragmentDisplay: Fragment){
        val transaction = supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

        // Hide all of the fragments
        for (fragment in supportFragmentManager.fragments) {
            transaction.hide(fragment!!)
        }

        if (fragmentDisplay.isAdded) {
            // When fragment was previously added - show it
            transaction.show(fragmentDisplay)
        } else {
            // When fragment is adding first time - add it
            transaction.add(R.id.fragmentContainerView, fragmentDisplay)
        }

        transaction.commit()
    }

    fun openHomeFragment() {
        replaceFragment(HomeFragment())
        binding.floatingActionButton.visibility = View.VISIBLE
    }

    fun showLoading() {
        //Inflate the dialog as custom view
        loadingDialog = LayoutInflater.from(this).inflate(R.layout.loading_layout, null)

        //AlertDialogBuilder
        val loadingDialogBuilder = AlertDialog.Builder(this).setView(loadingDialog)

        //show dialog
        loadingDialogInstance = loadingDialogBuilder.show()
        loadingDialogInstance.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun dismissLoading(){
        loadingDialogInstance.dismiss()
    }
}