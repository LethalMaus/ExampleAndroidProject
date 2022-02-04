package com.lethalmaus.exampleandroidproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lethalmaus.exampleandroidproject.databinding.ActivityMainBinding
import com.lethalmaus.exampleandroidproject.title.favourite.FavouritesFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.frameContainer, FavouritesFragment(), FavouritesFragment::class.java.simpleName)
                .addToBackStack(FavouritesFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onBackPressed() {
        when {
            supportFragmentManager.backStackEntryCount > 1 -> supportFragmentManager.popBackStack()
            else -> finish()
        }
    }
}