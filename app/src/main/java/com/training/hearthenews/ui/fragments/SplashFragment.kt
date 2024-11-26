package com.training.hearthenews.ui.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.training.hearthenews.R
import com.training.hearthenews.databinding.FragmentSplash2Binding


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplash2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentSplash2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rotation = ObjectAnimator.ofFloat(binding.newsLogo, "rotationY", 0f, 360f)
        rotation.duration = 3000
        rotation.start()

        val navHostFragment =
            requireActivity().supportFragmentManager.findFragmentById(R.id.navigation) as NavHostFragment
        val navController = navHostFragment.navController


        Handler(Looper.getMainLooper()).postDelayed({
            if(isAdded){
                findNavController().navigate(R.id.action_splashFragment_to_headLinesFragment)

            }
        },3000)
    }


}