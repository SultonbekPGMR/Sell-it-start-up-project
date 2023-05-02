package com.sultonbek1547.sellitstartupproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentMainBinding
import com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments.ChatFragmentNav
import com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments.HomeFragment
import com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments.LikedFragment
import com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments.ProfileFragment
import com.sultonbek1547.sellitstartupproject.utils.loadLikedProductsList


class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private var isEmpty = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        loadLikedProductsList()
        if (isEmpty) {
            switchFragment(HomeFragment())
            isEmpty = false
        }

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.navigation_post) {
                findNavController().navigate(R.id.postFragment)
                return@setOnItemSelectedListener true
            }
            switchFragment(getFragment(menuItem.itemId))
            true
        }


        return binding.root
    }


    private fun getFragment(itemId: Int): Fragment {
        return when (itemId) {
            R.id.navigation_home -> {
                HomeFragment()

            }
            R.id.navigation_liked -> {
                LikedFragment()

            }
            R.id.navigation_chats -> {
                ChatFragmentNav()

            }
            R.id.navigation_profile -> {
                ProfileFragment()

            }

            else -> HomeFragment()
        }

    }

    private fun switchFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}