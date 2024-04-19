package com.codeg.libreria

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.nav_view)

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
}
