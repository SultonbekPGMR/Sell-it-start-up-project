package com.sultonbek1547.sellitstartupproject.utils.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.RvItemProductBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.models.MyProduct

class ProductsRvAdapter(
    val function: (MyProduct) -> Unit,
    val btnLikeClicked: (Boolean, MyProduct) -> Unit,
) :
    RecyclerView.Adapter<ProductsRvAdapter.Vh>() {

    var productList: List<MyProduct> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class Vh(private val itemRvBinding: RvItemProductBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(product: MyProduct, position: Int) {
            itemRvBinding.apply {
                productName.text = product.productName
                productPrice.text = product.productPrice
                productLocation.text = product.productOwnerLocation
                productTime.text = product.productPostedDataAndTime
                Picasso.get().load(product.listOfProductImageLinks.first())
                    .into(productImage, object : Callback {
                        override fun onSuccess() {
                            imageProgress.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {

                        }
                    })

                root.setOnClickListener {
                    function(product)
                }

                btnLike.setOnClickListener {
                    if (btnLike.tag == "red") {
                        btnLike.setImageResource(R.drawable.navigation_heart)
                        btnLike.tag = "white"
                        btnLikeClicked(false, product)
                        return@setOnClickListener
                    }
                    btnLikeClicked(true, product)
                    btnLike.setImageResource(R.drawable.navigation_heart_selected)
                    btnLike.tag = "red"
                }

                MyConstants.likedProductIdsList?.let {
                    if (it.contains(product.productId)) {
                        btnLike.setImageResource(R.drawable.navigation_heart_selected)
                        btnLike.tag = "red"
                    } else {
                        btnLike.setImageResource(R.drawable.navigation_heart)
                        btnLike.tag = "white"
                    }
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(productList[position], position)


    override fun getItemCount(): Int = productList.size


}