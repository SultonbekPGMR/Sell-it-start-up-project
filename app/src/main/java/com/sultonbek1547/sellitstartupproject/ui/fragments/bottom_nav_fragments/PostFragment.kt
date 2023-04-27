package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentPostBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.db.firebase.MyFireBaseService
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

private const val TAG = "PostingNewProduct"

class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private var isImageSelected = false
    private var listOfSelectedImages = ArrayList<Uri>()
    private lateinit var editTextGroup: List<TextInputEditText>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(layoutInflater, container, false)



        init()




        return binding.root
    }

    private fun init() {
        requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        // Do custom work here
                        popBackStack()
                    }
                })

        editTextGroup = listOf<TextInputEditText>(
            binding.edtProductName,
            binding.edtDescription,
            binding.edtProductLocation,
            binding.edtProductPersonName,
            binding.edtProductPersonEmail,
            binding.edtProductPersonPhoneNumber
        )
        binding.tvSelectableProductPaid.isSelected = true
        initSelectableTextviewNewOrUser()
        initSelectableTextviewFreeOrPaid()
        handleSelectImageImageView()
        handleCategorySelection()
        handleBtnPostProduct()



        binding.toolbar.setNavigationOnClickListener { popBackStack() }
    }

    private fun popBackStack() {
        val navOptions =
            NavOptions.Builder().setPopUpTo(R.id.mainFragment, false).setLaunchSingleTop(true)
                .build()

        findNavController().navigate(R.id.mainFragment, null, navOptions)

    }

    private fun initSelectableTextviewFreeOrPaid() {
        binding.apply {

            tvSelectableProductFree.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    it.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductFree.setTextColor(resources.getColor(R.color.dark_blue))

                } else {
                    it.isSelected = true
                    it.background = getDrawable(
                        requireContext(), R.drawable.selectable_text_background_selected
                    )
                    tvSelectableProductFree.setTextColor(resources.getColor(R.color.white))
                    onlyViewContainerFee.visibility = View.GONE


                    tvSelectableProductPaid.isSelected = false
                    tvSelectableProductPaid.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductPaid.setTextColor(resources.getColor(R.color.dark_blue))

                }
            }
            tvSelectableProductPaid.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    it.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductPaid.setTextColor(resources.getColor(R.color.dark_blue))
                    onlyViewContainerFee.visibility = View.GONE

                } else {
                    it.isSelected = true
                    it.background = getDrawable(
                        requireContext(), R.drawable.selectable_text_background_selected
                    )
                    tvSelectableProductPaid.setTextColor(resources.getColor(R.color.white))
                    onlyViewContainerFee.visibility = View.VISIBLE


                    tvSelectableProductFree.isSelected = false
                    tvSelectableProductFree.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductFree.setTextColor(resources.getColor(R.color.dark_blue))

                }
            }

        }

    }


    private fun handleBtnPostProduct() {
        binding.btnPostProduct.setOnClickListener {

            var edtState = false
            editTextGroup.forEach {
                if (it.text.toString().isEmpty()) {
                    it.error = "bu satr to'ldirilishi shart"
                    edtState = true
                }
            }
            if (edtState) {
                showToast("Bo'sh satrlarni to'ldiring")
                return@setOnClickListener
            }

            if (!isImageSelected) {
                showToast("Rasmlarni qo'shing!")
                return@setOnClickListener
            }

            if (binding.edtProductCategory.text.toString().isEmpty()) {
                showToast("Bo'limni tanlang!")
                return@setOnClickListener
            }

            if (!binding.tvSelectableProductNew.isSelected && !binding.tvSelectableProductUsed.isSelected) {
                showToast("Holatini tanlang!")
                return@setOnClickListener
            }
            if (!binding.tvSelectableProductPaid.isSelected && !binding.tvSelectableProductFree.isSelected) {
                showToast("Narxini belgilang!")
                return@setOnClickListener
            }

            var edtProductPrice = binding.edtProductPrice.text.toString().trim()
            if (binding.tvSelectableProductPaid.isSelected && edtProductPrice.isEmpty()) {
                showToast("Narxini belgilang!")
                return@setOnClickListener
            }

            if (edtProductPrice.isEmpty()) edtProductPrice = "0"

            val parentCategory = binding.edtProductCategory.text.toString().substringBefore(" -> ")
            val childCategory = binding.edtProductCategory.text.toString().substringAfter(" -> ")


            val productStatus = if (binding.tvSelectableProductNew.isSelected) {
                "Yangi"
            } else {
                "Ishlatilgan"
            }

            val edtProductName = binding.edtProductName.text.toString().trim()
            val edtDescription = binding.edtDescription.text.toString().trim()
            val edtOwnerLocation = binding.edtProductLocation.text.toString().trim()
            val edtOwnerName = binding.edtProductPersonName.text.toString().trim()
            val edtOwnerEmail = binding.edtProductPersonEmail.text.toString().trim()
            val edtOwnerPhoneNumber = binding.edtProductPersonPhoneNumber.text.toString().trim()

            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Yuklanmoqda...")
            progressDialog.setCancelable(false)
            progressDialog.show()
            CoroutineScope(Dispatchers.IO).launch {
                val listOfImageLinks = saveImagesToFirebase()
                val product = MyProduct(
                    "id",
                    listOfImageLinks, //  here list is coming empty
                    edtProductName,
                    parentCategory,
                    childCategory,
                    edtDescription,
                    productStatus,
                    edtOwnerLocation,
                    edtOwnerName,
                    edtOwnerPhoneNumber,
                    edtOwnerEmail,
                    "27 april 16:52",
                    edtProductPrice
                )

                postProductToFireStore(product)
                // hide the progress dialog on the main thread after all the work is done
                withContext(Dispatchers.Main) {
                    progressDialog.dismiss()
                    withContext(Dispatchers.Main) {
                        popBackStack()
                    }
                }

            }


        }
    }

    private suspend fun saveImagesToFirebase(): List<String> {
        val listOfImageLinks = mutableListOf<String>()
        listOfSelectedImages.forEach {
            val imageLink = MyFireBaseService().postImageToStorage(UUID.randomUUID().toString(), it)
            listOfImageLinks.add(imageLink)
        }

        Log.e("FirebaseException", "getProductsAsync: $listOfImageLinks")

        return listOfImageLinks //  here list could be empty right
    }

    private fun handleCategorySelection() {
        binding.listenerContainerOfCategory.setOnClickListener {
            findNavController().navigate(
                R.id.displayCategoriesFragment,
                bundleOf("toolbarTitle" to "Bo'limlar", "categoryStep" to 1)
            )
        }
    }

    private fun handleSelectImageImageView() {
        binding.onlyViewTvSelectImageContainer.setOnClickListener {
            getMultipleImagesContent.launch("image/*")

        }

    }

    private fun initSelectableTextviewNewOrUser() {
        binding.apply {

            tvSelectableProductNew.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    it.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductNew.setTextColor(resources.getColor(R.color.dark_blue))

                } else {
                    it.isSelected = true
                    it.background = getDrawable(
                        requireContext(), R.drawable.selectable_text_background_selected
                    )
                    tvSelectableProductNew.setTextColor(resources.getColor(R.color.white))

                    tvSelectableProductUsed.isSelected = false
                    tvSelectableProductUsed.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductUsed.setTextColor(resources.getColor(R.color.dark_blue))

                }
            }
            tvSelectableProductUsed.setOnClickListener {
                if (it.isSelected) {
                    it.isSelected = false
                    it.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductUsed.setTextColor(resources.getColor(R.color.dark_blue))

                } else {
                    it.isSelected = true
                    it.background = getDrawable(
                        requireContext(), R.drawable.selectable_text_background_selected
                    )
                    tvSelectableProductUsed.setTextColor(resources.getColor(R.color.white))


                    tvSelectableProductNew.isSelected = false
                    tvSelectableProductNew.background =
                        getDrawable(requireContext(), R.drawable.selectable_text_background)
                    tvSelectableProductNew.setTextColor(resources.getColor(R.color.dark_blue))

                }
            }

        }
    }

    private val getMultipleImagesContent = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { images: List<Uri>? ->
        images ?: return@registerForActivityResult

        listOfSelectedImages.clear()
        images.forEachIndexed { index, imageUri ->
            if (index < 5) {
                addImageViewToSelectedImagesContainer(imageUri)
                listOfSelectedImages.add(imageUri)
            }
        }
        if (images.isNotEmpty()) {
            binding.onlyViewTvSelectImageContainer.visibility = View.INVISIBLE
            isImageSelected = true
        } else {
            isImageSelected = false
        }
    }


    private fun addImageViewToSelectedImagesContainer(imageUri: Uri) {
        val imageView = ImageView(context)
        imageView.setImageURI(imageUri)
        val params = LinearLayout.LayoutParams(
            400, LinearLayout.LayoutParams.MATCH_PARENT
        )
        imageView.layoutParams = params
        imageView.scaleType = ImageView.ScaleType.FIT_XY
        binding.selectedImagesContainer.addView(imageView)

        // Set the margin on all but the first ImageView
        if (binding.selectedImagesContainer.childCount > 1) {
            val marginParams = imageView.layoutParams as ViewGroup.MarginLayoutParams
            marginParams.marginStart = 50
        }
    }

    override fun onResume() {
        super.onResume()
        MyConstants.selectedCategory?.let {
            binding.edtProductCategory.setText(it)
            MyConstants.selectedCategory = null
        }
        if (MyConstants.isPoppingBackFromCategoryFragment) {
            MyConstants.isPoppingBackFromCategoryFragment = false
            binding.onlyViewTvSelectImageContainer.visibility = View.INVISIBLE
            listOfSelectedImages.forEach { imageUri ->
                addImageViewToSelectedImagesContainer(imageUri)
            }
        }
    }


    private suspend fun postProductToFireStore(product: MyProduct) =
        CoroutineScope(Dispatchers.IO).launch {
            MyFireBaseService().postProduct(product)

        }


}