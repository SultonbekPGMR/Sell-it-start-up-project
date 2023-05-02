package com.sultonbek1547.sellitstartupproject.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.sultonbek1547.sellitstartupproject.databinding.ImageItemLayoutBinding

class ImagePagerAdapter(private val imageLinks: List<String>) : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageLinks[position])
    }

    override fun getItemCount(): Int = imageLinks.size

    inner class ImageViewHolder(private val binding: ImageItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(imageLink: String) {
            Picasso.get().load(imageLink)
                .into(binding.productImage, object : Callback {
                    override fun onSuccess() {
                        binding.imageProgress.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {

                    }
                })
        }
    }
}