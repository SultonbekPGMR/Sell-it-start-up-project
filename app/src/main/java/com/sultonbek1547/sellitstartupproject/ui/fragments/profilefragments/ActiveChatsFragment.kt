package com.sultonbek1547.sellitstartupproject.ui.fragments.profilefragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentChatActiveBinding
import com.sultonbek1547.sellitstartupproject.databinding.FragmentChatBinding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.adapters.UsersAdapter

class ActiveChatsFragment : Fragment() {
    private lateinit var binding: FragmentChatActiveBinding
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var reference: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChatActiveBinding.inflate(layoutInflater, container, false)
        binding.progressBar.visibility = View.VISIBLE
        reference = FirebaseDatabase.getInstance().getReference("chats")

        MySharedPreference.init(requireContext())


        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // The reference exists
                    val keyChatsList = ArrayList<String>()
                    snapshot.children.forEach {
                        it.key?.let { it1 -> keyChatsList.add(it1) }
                    }


                    val rvList = ArrayList<User>()
                    val user = MySharedPreference.user
                    keyChatsList.forEach { chatKey ->
                        MyConstants.userList.forEach { userInDb ->
                            userInDb.uid?.let {
                                if (chatKey == it + user?.uid || chatKey == user?.uid + it) {
                                    rvList.add(userInDb)
                                }
                            }
                        }

                    }
                    usersAdapter = UsersAdapter(rvList) { user: User, position: Int ->
                        listItemClicked(
                            user,
                            position
                        )
                    }
                    if (rvList.isEmpty()) {
                        binding.containerEmpty.visibility = View.VISIBLE
                    } else {
                        binding.containerEmpty.visibility = View.GONE
                    }

                    binding.progressBar.visibility = View.GONE
                    binding.myRv.adapter = usersAdapter

                }else{
                    binding.containerEmpty.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun listItemClicked(user: User, position: Int) {
        findNavController().navigate(R.id.chatFragment, bundleOf("user" to user))
    }
}