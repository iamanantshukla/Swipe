package com.dev334.swipe.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev334.swipe.R
import com.dev334.swipe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener{
            replaceFragment(AddProductFragment())
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

}