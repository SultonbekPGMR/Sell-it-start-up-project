package com.sultonbek1547.sellitstartupproject.utils.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.sultonbek1547.sellitstartupproject.databinding.RvUserItemBinding
import com.sultonbek1547.sellitstartupproject.models.User

class UsersAdapter(private val  list: ArrayList<User>, val function: (User, Int) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.Vh>() {

    inner class Vh(private val itemRvBinding: RvUserItemBinding) :
        RecyclerView.ViewHolder(itemRvBinding.root) {
        fun onBind(user: User, position: Int) {
            itemRvBinding.itemCard.setOnClickListener {
                function(user, position)
            }

            itemRvBinding.tvName.text = user.name
            Picasso.get()
                .load(user.imageLink)
                .into(itemRvBinding.image)



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(RvUserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) =
        holder.onBind(list[position], position)


    override fun getItemCount(): Int = list.size


}