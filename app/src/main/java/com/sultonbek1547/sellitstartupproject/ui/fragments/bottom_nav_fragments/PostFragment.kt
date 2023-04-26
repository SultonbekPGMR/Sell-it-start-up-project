package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentPostBinding

class PostFragment : Fragment() {


    private lateinit var binding: FragmentPostBinding
    private var isImageSelected = false
    private var listOfSelectedImages = ArrayList<Uri>()
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
        initSelectableTextviewPersonOrBusiness()
        handleSelectImageImageView()

    }

    private fun handleSelectImageImageView() {
        binding.onlyViewTvSelectImageContainer.setOnClickListener {
            getMultipleImagesContent.launch("image/*")

        }

    }

    private fun initSelectableTextviewPersonOrBusiness() {
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
                        requireContext(),
                        R.drawable.selectable_text_background_selected
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
                        requireContext(),
                        R.drawable.selectable_text_background_selected
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

}