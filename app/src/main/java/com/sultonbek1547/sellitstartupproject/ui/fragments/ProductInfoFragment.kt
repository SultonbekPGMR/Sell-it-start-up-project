package com.sultonbek1547.sellitstartupproject.ui.fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.BottomSheetBinding
import com.sultonbek1547.sellitstartupproject.databinding.FragmentProductInfoBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.db.firebase.MyRemoteRepository
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.ui.viewmodels.MyProductsViewModel
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.adapters.ImagePagerAdapter
import com.sultonbek1547.sellitstartupproject.utils.removeLikedProductFromList
import com.sultonbek1547.sellitstartupproject.utils.uploadLikedProductToList


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
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Diqqat")
                builder.setMessage("Bu e'loningizni o'chirmoqchimisiz?")
                builder.setPositiveButton(
                    "Xa"
                ) { _, _ ->
                    MyFireBaseService().deleteProduct(product = product, MySharedPreference.user!!)
                    MyProductsViewModel(MyRemoteRepository(MyFireBaseService())).productList.value?.remove(product)
                    findNavController().popBackStack()
                }
                builder.setNegativeButton(
                    "Yo'q"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }

        initViews(product)

        binding.viewPager.adapter = ImagePagerAdapter(product.listOfProductImageLinks)



        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
        }.attach()



        return binding.root
    }

    private fun initViews(product: MyProduct) {
        var isMenuItemClicked = MyConstants.likedProductsList?.contains(product)
        binding.apply {
            tvProductDate.text = product.productPostedDataAndTime
            tvProductName.text = product.productName
            tvProductStatus.text = product.productStatus
            tvProductLocation.text = product.productOwnerLocation
            tvProductPrice.text = product.productPrice
//            tvDescription.text = product.productDescription

        }


        val colorDarkBlue = ContextCompat.getColor(requireContext(), R.color.dark_blue)
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        binding.scrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            val maxScrollY = binding.scrollView.getChildAt(0)?.let {
                (it.height - binding.scrollView.height.takeIf { height -> height > 0 }!!) ?: 0
            } ?: 0
            val scrollPercentage = if (maxScrollY > 0) scrollY * 100 / maxScrollY else 0

            // calculate the alpha value based on the scroll percentage
            val alpha = when {
                scrollPercentage <= 0 -> 0
                scrollPercentage >= 80 -> 255
                else -> (scrollPercentage / 80.0 * 255).toInt()
            }
            if (alpha > 220) {
                binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
                binding.toolbar.menu.findItem(R.id.menu_liked).iconTintList =
                    ColorStateList.valueOf(colorDarkBlue)
            } else {
                binding.toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_white)
                binding.toolbar.menu.findItem(R.id.menu_liked).iconTintList =
                    ColorStateList.valueOf(colorWhite)
            }

            // set the toolbar background color with the adjusted alpha value
            binding.toolbar.setBackgroundColor(Color.argb(alpha, 255, 255, 255))
            binding.toolbar.elevation = if (scrollPercentage > 0) 4F else 1F
        }


        // toolbar menuItem
        binding.apply {
            binding.toolbar.menu.findItem(R.id.menu_liked).iconTintList =
                ColorStateList.valueOf(colorWhite)
            binding.toolbar.navigationIcon?.setTint(Color.WHITE)
            if (isMenuItemClicked == true) {
                toolbar.menu.findItem(R.id.menu_liked).setIcon(R.drawable.navigation_heart_selected)
            }

            toolbar.setOnMenuItemClickListener { menuItem ->
                isMenuItemClicked?.let {
                    if (it) {
                        isMenuItemClicked = false
                        menuItem.setIcon(R.drawable.navigation_heart)
                        removeLikedProductFromList(product)
                    } else {
                        isMenuItemClicked = true
                        menuItem.setIcon(R.drawable.navigation_heart_selected)
                        uploadLikedProductToList(product)
                    }
                }


                true
            }

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