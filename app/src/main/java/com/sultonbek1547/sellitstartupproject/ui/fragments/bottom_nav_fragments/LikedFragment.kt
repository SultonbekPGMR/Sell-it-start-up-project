package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentHomeBinding
import com.sultonbek1547.sellitstartupproject.databinding.FragmentLikedBinding


class LikedFragment : Fragment() {

    private lateinit var binding: FragmentLikedBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLikedBinding.inflate(layoutInflater, container, false)



        return binding.root
    }

}