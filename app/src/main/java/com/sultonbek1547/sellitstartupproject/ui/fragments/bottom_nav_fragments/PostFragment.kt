package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sultonbek1547.sellitstartupproject.databinding.FragmentLikedBinding
import com.sultonbek1547.sellitstartupproject.databinding.FragmentPostBinding

class PostFragment : Fragment() {


    private lateinit var binding: FragmentPostBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)


        return binding.root
    }

}