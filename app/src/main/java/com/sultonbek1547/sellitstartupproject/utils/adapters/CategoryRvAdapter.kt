package com.sultonbek1547.sellitstartupproject.utils.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.RvCategoryItemBinding

class CategoryRvAdapter(
    val context: Context,
    private val list: List<String>,
    val itemClickedFunction: (String) -> Unit,
) : RecyclerView.Adapter<CategoryRvAdapter.Vh>() {

    var selectedItemPosition = 0

    inner class Vh(private val itemRvBinding: RvCategoryItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {

        fun onBind(categoryName: String, position: Int) {
            itemRvBinding.apply {
                tvName.text = categoryName
                if (selectedItemPosition == position) {
                    tvName.setTypeface(null, Typeface.BOLD)
                    tvName.setTextColor(Color.WHITE)
                    tvName.setBackgroundResource(R.drawable.item_category_selected)
                } else {
                    tvName.setTypeface(null, Typeface.NORMAL)
                    tvName.setTextColor(context.resources.getColor(R.color.dark_blue))
                    tvName.setBackgroundResource(R.drawable.item_category_default)
                }

                tvName.setOnClickListener {
                    if (selectedItemPosition != position) {
                        itemClickedFunction(categoryName)
                        notifyItemChanged(selectedItemPosition)
                        selectedItemPosition = position
                        notifyItemChanged(selectedItemPosition)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            RvCategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}