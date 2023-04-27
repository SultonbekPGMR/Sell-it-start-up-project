package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sultonbek1547.sellitstartupproject.databinding.FragmentHomeBinding
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.utils.adapters.ProductsRvAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {


    private lateinit var binding: FragmentHomeBinding
    private lateinit var rvAdapter: ProductsRvAdapter
    private var listOfProducts = ArrayList<MyProduct>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        binding.myRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        loadProductsToRecyclerView()



        return binding.root
    }

    private fun loadProductsToRecyclerView() = CoroutineScope(Dispatchers.IO).launch {
        listOfProducts = MyFireBaseService().getProductsAsync().await()

        withContext(Dispatchers.Main) {
            binding.myRv.adapter = ProductsRvAdapter(listOfProducts)
        }


    }

}