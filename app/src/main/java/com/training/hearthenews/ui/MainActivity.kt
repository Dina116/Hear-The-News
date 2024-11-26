package com.training.hearthenews.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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
import com.training.hearthenews.R
import com.training.hearthenews.databinding.ActivityMainBinding
import com.training.hearthenews.db.ArticleDataBase
import com.training.hearthenews.repository.NewsRepository
import com.training.hearthenews.ui.fragments.HeadLinesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var newsViewModel: NewsViewModel
  // lateinit var categoryViewModel: CategoryViewModel
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val newsRepository = NewsRepository(ArticleDataBase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        newsViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
           navController = navHostFragment.navController
           binding.bottomNavigationView.setupWithNavController(navController)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

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
                else -> false
            }
        }




    }
//    val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment
//    navController = navHostFragment.navController
//    val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
//    //NavigationUI.setupWithNavController(bottomNavigationView, navController)
//
//    bottomNavigationView.setOnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.headlinesFragment -> {
//                navController.navigate(R.id.headlinesFragment) // Navigate to HomeFragment
//                true
//            }
//
//            R.id.favouritesFragment-> {
//                navController.navigate(R.id.favouriteFragment) // Navigate to FavoritesFragment
//                true
//            }
//            R.id.searchFragment -> {
//                navController.navigate(R.id.searchFragment) // Navigate to SearchFragment
//                true
//            }
//
//
//            else -> false
//        }
//
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu, menu)
//        //applyFontToMenuItem(menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.favouriteFragment -> {
//                findNavController(R.id.newsNavHostFragment).navigate(R.id.favouriteFragment)
//                true
//            }
//            R.id.searchFragment -> {
//                findNavController(R.id.newsNavHostFragment).navigate(R.id.searchFragment)
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
}