package com.sultonbek1547.sellitstartupproject.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sultonbek1547.sellitstartupproject.databinding.RvItemProductBinding
import com.sultonbek1547.sellitstartupproject.models.MyProduct

class ProductsRvAdapter(private val list: ArrayList<MyProduct>) :
    RecyclerView.Adapter<ProductsRvAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvItemProductBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(product: MyProduct, position: Int) {
            itemRvBinding.apply {
                productName.text = product.productName
                productPrice.text = product.productPrice
                productLocation.text = product.productOwnerLocation
                productTime.text = product.productPostedDataAndTime
                Picasso.get().load(product.listOfProductImageLinks.first()).into(productImage)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}