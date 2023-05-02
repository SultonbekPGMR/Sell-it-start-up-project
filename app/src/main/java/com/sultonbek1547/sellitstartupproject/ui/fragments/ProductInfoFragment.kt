package com.sultonbek1547.sellitstartupproject.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.BottomSheetBinding
import com.sultonbek1547.sellitstartupproject.databinding.FragmentProductInfoBinding
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.adapters.ImagePagerAdapter
import com.sultonbek1547.sellitstartupproject.utils.showToast


class ProductInfoFragment : Fragment() {

    private lateinit var binding: FragmentProductInfoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProductInfoBinding.inflate(layoutInflater, container, false)
        MySharedPreference.init(requireContext())
        val product = arguments?.getSerializable("product") as MyProduct

        if (product.userId == MySharedPreference.user?.uid) {
            binding.containerBtnCallAndSms.visibility = View.GONE
            binding.containerBtnDelete.visibility = View.VISIBLE

            binding.btnDeleteProduct.setOnClickListener {
                showToast("delete")
            }
        }

        initViews(product)

        binding.viewPager.adapter = ImagePagerAdapter(product.listOfProductImageLinks)



        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->

        }.attach()


        binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val drawable = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.baseline_arrow_back_white

            )
            if (scrollY > oldScrollY) {
                // when user scrolls down set toolbar elevation to 4dp
                binding.toolbar.elevation = 4F;
                binding.toolbar.setBackgroundColor(resources.getColor(R.color.dark_blue));
            }
            if (scrollY < oldScrollY) {
                // when user scrolls up keep binding.toolbar elevation at 4dp
                binding.toolbar.elevation = 4F;
                binding.toolbar.setBackgroundColor(resources.getColor(R.color.blue));

            }

            if (scrollY == 0) {
                // if user is not scrolling it means
                // that he is at top of page
                binding.toolbar.elevation = 1F; // required or it will overlap linear layout
                binding.toolbar.setBackgroundColor(Color.TRANSPARENT); // required to delete elevation shadow

            }
        }



        return binding.root
    }

    private fun initViews(product: MyProduct) {


        binding.apply {
            tvProductDate.text = product.productPostedDataAndTime
            tvProductName.text = product.productName
            tvProductStatus.text = product.productStatus
            tvProductLocation.text = product.productOwnerLocation
            tvProductPrice.text = product.productPrice
//            tvDescription.text = product.productDescription
        }



        binding.btnCallOrSms.setOnClickListener {
            showCallOrSmsDialog(product.productOwnerPhoneNumber)
        }

        binding.btnChat.setOnClickListener {
            findNavController().navigate(R.id.chatFragment, bundleOf("product" to product))
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showCallOrSmsDialog(productOwnerPhoneNumber: String) {
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.AppBottomSheetDialogTheme)
        val bottomSheetBinding = BottomSheetBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetBinding.apply {
            tvUserNumber.text = productOwnerPhoneNumber

            btnCall.setOnClickListener {
                val callIntent = Intent(Intent.ACTION_DIAL)
                callIntent.data = Uri.parse("tel:${productOwnerPhoneNumber}")
                startActivity(callIntent)
            }
            btnSms.setOnClickListener {
                val smsUri = Uri.parse("smsto:${productOwnerPhoneNumber}")
                val smsIntent = Intent(Intent.ACTION_SENDTO, smsUri)
                smsIntent.putExtra("sms_body", "Assalomu alaykum")
                startActivity(smsIntent)
            }

            btnClose.setOnClickListener {
                if (bottomSheetDialog.isShowing) bottomSheetDialog.dismiss()
            }

        }

        bottomSheetDialog.show()

    }


}