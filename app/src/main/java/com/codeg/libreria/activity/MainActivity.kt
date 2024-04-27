package com.codeg.libreria.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.codeg.libreria.fragment.AdminFragment
import com.codeg.libreria.fragment.BooksFragment
import com.codeg.libreria.fragment.HomeFragment
import com.codeg.libreria.fragment.LendingFragment
import com.codeg.libreria.R
import com.codeg.libreria.fragment.UserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private var doubleBackToExitPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.nav_view)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putLong("lastLoginTime", System.currentTimeMillis()).apply()

        // Check if the intent has an extra to determine which fragment to open
        val fragmentToOpen = intent.getStringExtra("fragmentToOpen")

        // Open the appropriate fragment based on the extra passed in the intent
        when (fragmentToOpen) {

            "BooksFragment" -> {
                openFragment(BooksFragment())
                bottomNavigationView.selectedItemId = R.id.navigation_book // Select the "Book" icon
            }
            "UserFragment" -> {
                openFragment(UserFragment())
                bottomNavigationView.selectedItemId = R.id.navigation_user // Select the "User" icon
            }
            "LendingFragment" -> {
                openFragment(LendingFragment())
                bottomNavigationView.selectedItemId =
                    R.id.navigation_lending // Select the "Lending" icon
            }
            "AdminFragment" -> {
                openFragment(AdminFragment())
                bottomNavigationView.selectedItemId =
                    R.id.navigation_admin // Select the "Book" icon
            }

            else -> openFragment(HomeFragment()) // Default fragment
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            // Handle navigation item clicks
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.navigation_book -> {
                    openFragment(BooksFragment())
                    true
                }
                R.id.navigation_user -> {
                    openFragment(UserFragment())
                    true
                }
                R.id.navigation_lending -> {
                    openFragment(LendingFragment())
                    true
                }
                R.id.navigation_admin -> {
                    openFragment(AdminFragment())
                    true
                }
                else -> false
            }
        }
    }


    private fun openFragment(fragment: androidx.fragment.app.Fragment){
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mainFragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            openFragment(HomeFragment())
            super.onBackPressed()
            finish()
            return
        }

        openFragment(HomeFragment())
        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show()

        // Reset the flag after a certain delay
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)

    }
}
