package com.training.hearthenews.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.training.hearthenews.R
import com.training.hearthenews.databinding.ActivityMainBinding
import com.training.hearthenews.db.ArticleDataBase
import com.training.hearthenews.repository.NewsRepository
import com.training.hearthenews.ui.fragments.HeadLinesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    lateinit var newsViewModel: NewsViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       FirebaseApp.initializeApp(this)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth.currentUser == null) {
            navController.navigate(R.id.signupFragment)
        }
//
//        Handler(Looper.getMainLooper()).post {
            // Access Firebase here


//        }



        val newsRepository = NewsRepository(ArticleDataBase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        newsViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashFragment, R.id.loginFragment, R.id.signupFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    navController.navigate(R.id.headLinesFragment)
                    Toast.makeText(this, "Go To Headlines", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.favouritesFragment -> {
                    navController.navigate(R.id.favouriteFragment)
                    Toast.makeText(this, "Go To favourites", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment2)
                    Toast.makeText(this, "Go To search", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.trendFragment -> {
                    navController.navigate(R.id.trendsFragment)
                    Toast.makeText(this, "Go To trends", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.setOnItemReselectedListener { item ->
            // Handle reselection of an item, if needed
            when (item.itemId) {
                R.id.headlinesFragment -> {
                    navController.navigate(R.id.headlinesFragment)
                    Toast.makeText(this, "Go To Headlines", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.favouritesFragment -> {
                    navController.navigate(R.id.favouriteFragment)
                    Toast.makeText(this, "Go To favourites", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment2)
                    Toast.makeText(this, "Go To search", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.trendFragment -> {
                    navController.navigate(R.id.trendsFragment)
                    Toast.makeText(this, "Go To trends", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }






    }

}