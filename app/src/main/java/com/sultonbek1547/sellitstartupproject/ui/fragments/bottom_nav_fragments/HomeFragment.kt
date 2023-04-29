package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sultonbek1547.sellitstartupproject.databinding.FragmentHomeBinding
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.db.firebase.MyRemoteRepository
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyProductsViewModel
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyViewModelFactory
import com.sultonbek1547.sellitstartupproject.utils.adapters.ProductsRvAdapter
import com.sultonbek1547.sellitstartupproject.utils.removeLikedProductIdFromList
import com.sultonbek1547.sellitstartupproject.utils.showToast
import com.sultonbek1547.sellitstartupproject.utils.uploadLikedProductIdToList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewAdapter: ProductsRvAdapter
    private lateinit var productsViewModel: MyProductsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        val viewModelFactory = MyViewModelFactory(MyRemoteRepository(MyFireBaseService()))
        productsViewModel =
            ViewModelProvider(this, viewModelFactory).get(MyProductsViewModel::class.java)

        initRecyclerView()

        return binding.root
    }


    private fun initRecyclerView() {
        binding.progressBar.visibility = View.VISIBLE
        recyclerViewAdapter = ProductsRvAdapter(
            { selectedItem: MyProduct -> listItemClicked(selectedItem) },
            { state: Boolean, product: MyProduct -> handleBtnLikeClicks(state, product) }
        )
        binding.myRv.adapter = recyclerViewAdapter
        displayProductsList()
    }

    private fun handleBtnLikeClicks(state: Boolean, product: MyProduct) {
        if (state) {
            uploadLikedProductIdToList(product.productId)
            return
        }
        removeLikedProductIdFromList(product.productId)
    }

    private fun displayProductsList() {
        productsViewModel.productList.observe(requireActivity(), Observer {
            recyclerViewAdapter.productList = it
            if (binding.progressBar.visibility == View.VISIBLE) {
                binding.progressBar.visibility = View.GONE
            }
        })

    }

    private fun listItemClicked(selectedItem: MyProduct) {
        showToast("item clicked")
    }
}