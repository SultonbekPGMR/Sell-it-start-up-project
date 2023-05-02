package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentHomeBinding
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.db.firebase.MyRemoteRepository
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyProductsViewModel
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyViewModelFactory
import com.sultonbek1547.sellitstartupproject.utils.adapters.ProductsRvAdapter
import com.sultonbek1547.sellitstartupproject.utils.loadLikedProductsList
import com.sultonbek1547.sellitstartupproject.utils.removeLikedProductFromList
import com.sultonbek1547.sellitstartupproject.utils.uploadLikedProductToList


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var recyclerViewAdapter: ProductsRvAdapter
    private lateinit var productsViewModel: MyProductsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        loadLikedProductsList()
        val viewModelFactory = MyViewModelFactory(MyRemoteRepository(MyFireBaseService()))
        productsViewModel =
            ViewModelProvider(this, viewModelFactory).get(MyProductsViewModel::class.java)

        initRecyclerView()

        MyFireBaseService().getUsersFromFirebaseAsList()
        return binding.root
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
            if (binding.progressBar.visibility == View.VISIBLE) {
                binding.progressBar.visibility = View.GONE
            }
        })

    }

    private fun listItemClicked(selectedItem: MyProduct) {
        findNavController().navigate(R.id.productInfoFragment, bundleOf("product" to selectedItem))
    }
}