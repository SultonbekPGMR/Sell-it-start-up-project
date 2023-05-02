package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentChatBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.adapters.UsersAdapter

class ChatFragmentNav : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private lateinit var usersAdapter: UsersAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)


        val rvList = ArrayList<User>()
        MyConstants.userList.forEach {
            if (it.uid != MySharedPreference.user?.uid) rvList.add(it)
        }.let {
            usersAdapter = UsersAdapter(rvList) { user: User, position: Int ->
                listItemClicked(
                    user,
                    position
                )
            }
            binding.myRv.adapter = usersAdapter
        }




        return binding.root
    }
    private fun listItemClicked(user: User, position: Int) {

        findNavController().navigate(R.id.chatFragment, bundleOf("user" to user))
    }
}