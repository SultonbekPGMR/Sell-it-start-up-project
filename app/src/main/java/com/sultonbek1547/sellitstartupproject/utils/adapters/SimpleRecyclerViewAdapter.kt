package com.sultonbek1547.sellitstartupproject.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sultonbek1547.sellitstartupproject.databinding.RvItemCategoryNamesBinding

class SimpleRecyclerViewAdapter(private val list: List<String>, val itemClickedFunction: (String) -> Unit) :
    RecyclerView.Adapter<SimpleRecyclerViewAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvItemCategoryNamesBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(categoryName: String, position: Int) {
            itemRvBinding.tvCategoryName.text = categoryName

            itemRvBinding.tvCategoryName.setOnClickListener {
                itemClickedFunction(categoryName)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(
            RvItemCategoryNamesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}