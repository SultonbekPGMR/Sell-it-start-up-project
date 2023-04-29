package com.sultonbek1547.sellitstartupproject.ui.fragments.profilefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.databinding.FragmentAboutAppBinding


class AboutAppFragment : Fragment() {
    private lateinit var binding: FragmentAboutAppBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAboutAppBinding.inflate(layoutInflater, container, false)


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}