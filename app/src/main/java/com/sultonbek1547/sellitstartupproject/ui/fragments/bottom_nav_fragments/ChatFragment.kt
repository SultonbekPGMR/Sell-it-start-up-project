package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sultonbek1547.sellitstartupproject.databinding.FragmentChatBinding

class ChatFragment : Fragment() {


    private lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)





        return binding.root
    }

}