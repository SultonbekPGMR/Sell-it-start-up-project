package com.sultonbek1547.sellitstartupproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentMainBinding
import com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments.*


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)


        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    switchFragment(HomeFragment())
                    true
                }
                R.id.navigation_liked -> {
                    switchFragment(LikedFragment())
                    true
                }
                R.id.navigation_post -> {
                    switchFragment(PostFragment())
                    true
                }
                R.id.navigation_chats -> {
                    switchFragment(ChatFragment())
                    true
                }
                R.id.navigation_profile -> {
                    switchFragment(ProfileFragment())
                    true
                }

                else -> false
            }

        }


        return binding.root
    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}