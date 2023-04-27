package com.sultonbek1547.sellitstartupproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentDisplayCategoriesBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.utils.adapters.SimpleRecyclerViewAdapter

class DisplayCategoriesFragment : Fragment() {

    private lateinit var binding: FragmentDisplayCategoriesBinding
    private var categoryStep: Int = 1
    private var toolbarTitle = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDisplayCategoriesBinding.inflate(layoutInflater, container, false)

        toolbarTitle = arguments?.getString("toolbarTitle")!!
        categoryStep = arguments?.getInt("categoryStep")!!


        if (toolbarTitle == "Bo'limlar") {
            loadListToRecyclerView(MyConstants.mainCategories)
        } else {
            loadListToRecyclerView(MyConstants.subCategories[toolbarTitle]!!)
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }

    private fun loadListToRecyclerView(list: List<String>) {
        binding.myRv.adapter =
            SimpleRecyclerViewAdapter(list) { selectedItem: String ->
                listItemClicked(selectedItem)
            }
    }

    private fun listItemClicked(selectedItem: String) {

        if (categoryStep == 2) {
            MyConstants.selectedCategory = "$toolbarTitle -> $selectedItem"
            findNavController().popBackStack(R.id.postFragment, false)
            return
        }

        findNavController().navigate(
            R.id.displayCategoriesFragment,
            bundleOf("toolbarTitle" to selectedItem, "categoryStep" to ++categoryStep)
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        MyConstants.isPoppingBackFromCategoryFragment = true
    }

}