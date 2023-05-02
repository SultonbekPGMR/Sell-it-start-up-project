package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sultonbek1547.sellitstartupproject.databinding.FragmentLikedBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.utils.adapters.ProductsRvAdapter
import com.sultonbek1547.sellitstartupproject.utils.removeLikedProductFromList
import com.sultonbek1547.sellitstartupproject.utils.showToast
import com.sultonbek1547.sellitstartupproject.utils.uploadLikedProductToList

class LikedFragment : Fragment() {

    private lateinit var binding: FragmentLikedBinding
    private lateinit var recyclerViewAdapter: ProductsRvAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLikedBinding.inflate(layoutInflater, container, false)

        initRv()

        return binding.root
    }

    private fun initRv() {
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
        MyConstants.likedProductsList?.let {
            if (it.isNotEmpty()) {
                recyclerViewAdapter.productList = it
            } else {
                binding.containerNoData.visibility = View.VISIBLE
            }
        }


    }


    private fun listItemClicked(selectedItem: MyProduct) {
        showToast("item clicked")
    }

    private fun handleBtnLikeClicks(state: Boolean, product: MyProduct, position: Int) {
        if (state) {
            uploadLikedProductToList(product)
            return
        }
        removeLikedProductFromList(product)
        recyclerViewAdapter.productList.remove(product)
        recyclerViewAdapter.notifyItemRemoved(position)
        recyclerViewAdapter.notifyItemRangeChanged(0, recyclerViewAdapter.itemCount)
        if (recyclerViewAdapter.productList.isEmpty()) binding.containerNoData.visibility = View.VISIBLE

    }

}