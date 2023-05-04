package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentHomeBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.db.firebase.MyRemoteRepository
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyProductsViewModel
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyViewModelFactory
import com.sultonbek1547.sellitstartupproject.utils.adapters.CategoryRvAdapter
import com.sultonbek1547.sellitstartupproject.utils.adapters.ProductsRvAdapter
import com.sultonbek1547.sellitstartupproject.utils.loadLikedProductsList
import com.sultonbek1547.sellitstartupproject.utils.removeLikedProductFromList
import com.sultonbek1547.sellitstartupproject.utils.uploadLikedProductToList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewAdapter: ProductsRvAdapter
    private lateinit var productsViewModel: MyProductsViewModel
    private var productList = ArrayList<MyProduct>()
    private var selectedCategory = "Barchasi"
    private var currentTextOnSearchView = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.searchView.clearFocus()

        val categoryNames = ArrayList<String>()
        categoryNames.add("Barchasi")
        categoryNames.addAll(MyConstants.mainCategories)
        binding.myCategoryRv.adapter = CategoryRvAdapter(
            requireContext(),
            categoryNames
        ) { selectedCategory: String ->
            categorySelected(selectedCategory)
        }



        loadLikedProductsList()
        val viewModelFactory = MyViewModelFactory(MyRemoteRepository(MyFireBaseService()))
        productsViewModel =
            ViewModelProvider(this, viewModelFactory).get(MyProductsViewModel::class.java)
        initRecyclerView()
        initSearchView()

        MyFireBaseService().getUsersFromFirebaseAsList()
        return binding.root
    }

    private fun categorySelected(selectedCategory: String) {
        this.selectedCategory = selectedCategory
        if (currentTextOnSearchView.isNotEmpty()) {
            filerList(currentTextOnSearchView)
            return
        }
        if (selectedCategory == "Barchasi") {
            recyclerViewAdapter.productList = productList
            binding.tvNoDataFound.visibility = View.GONE
            return
        }
        val filteredList = ArrayList<MyProduct>()
        productList.forEach {
            if (it.productParentCategory == selectedCategory) filteredList.add(it)
        }
        if (filteredList.isEmpty()) {
            recyclerViewAdapter.productList = filteredList
            binding.tvNoDataFound.visibility = View.VISIBLE
        } else {
            recyclerViewAdapter.productList = filteredList
            binding.tvNoDataFound.visibility = View.GONE
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null) {
                    filerList(p0)
                }
                return true
            }
        })
    }

    private fun filerList(p0: String) {
        if (p0.isEmpty()) {
            categorySelected(selectedCategory)
            binding.tvNoDataFound.visibility = View.GONE
            return
        }
        val filteredList = ArrayList<MyProduct>()

        productList.forEach {
            if (it.productName.lowercase()
                    .contains(p0.lowercase()) && (it.productParentCategory == selectedCategory || selectedCategory == "Barchasi")
            ) {
                filteredList.add(it)
            }
        }
        if (filteredList.isEmpty()) {
            recyclerViewAdapter.productList = filteredList
            binding.tvNoDataFound.visibility = View.VISIBLE
        } else {
            recyclerViewAdapter.productList = filteredList
            binding.tvNoDataFound.visibility = View.GONE
        }

    }


    private fun initRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        recyclerViewAdapter = ProductsRvAdapter(
            { selectedItem: MyProduct -> listItemClicked(selectedItem) }
        ) { state: Boolean, product: MyProduct, position: Int ->
            handleBtnLikeClicks(
                state,
                product,
                position
            )
        }
        binding.myRv.adapter = recyclerViewAdapter
        displayProductsList()
    }

    private fun handleBtnLikeClicks(state: Boolean, product: MyProduct, position: Int) {
        if (state) {
            uploadLikedProductToList(product)
            return
        }
        removeLikedProductFromList(product)
    }

    private fun displayProductsList() {
        productsViewModel.productList.observe(requireActivity(), Observer {
            recyclerViewAdapter.productList = it
            productList = it
            if (it.isEmpty()) {
                binding.containerEmpty.visibility = View.VISIBLE
            } else {
                binding.containerEmpty.visibility = View.GONE
            }
            if (binding.progressBar.visibility == View.VISIBLE) {
                binding.progressBar.visibility = View.GONE
            }
        })

    }

    private fun listItemClicked(selectedItem: MyProduct) {
        findNavController().navigate(R.id.productInfoFragment, bundleOf("product" to selectedItem))
    }


}