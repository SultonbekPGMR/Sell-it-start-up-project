package com.sultonbek1547.sellitstartupproject.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.sultonbek1547.sellitstartupproject.databinding.FragmentChat2Binding
import com.sultonbek1547.sellitstartupproject.db.MyConstants
import com.sultonbek1547.sellitstartupproject.models.MessageText
import com.sultonbek1547.sellitstartupproject.models.MyProduct
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.adapters.ChatAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChat2Binding
    private lateinit var databse: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var chatAdapter: ChatAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentChat2Binding.inflate(layoutInflater, container, false)
        MySharedPreference.init(requireContext())
        var user: User? = null
        val product: MyProduct? = arguments?.getSerializable("product") as MyProduct?
        user = if (product == null) {
            arguments?.getSerializable("user") as User?
        } else {
            MyConstants.getUser(product.userId)
        }
        binding.btnSend.visibility = View.GONE
        binding.tvUserStatusTime.visibility = View.INVISIBLE
        binding.edtMessage.isActivated = true
        binding.edtMessage.isPressed = true
        binding.edtMessage.requestFocus()
        initViews(user!!)





        return binding.root
    }



    private fun initViews(user: User) {
        binding.tvUserName.text = user.name
        Picasso.get().load(user.imageLink).into(binding.userImage)
        binding.tvUserStatusTime.text = "last seen at " + user.statusTime

        databse = FirebaseDatabase.getInstance()
        reference = databse.getReference("chats")
            .child(MySharedPreference.user?.uid + user.uid)

        chatAdapter = ChatAdapter(ArrayList(), MySharedPreference.user?.uid!!)
        binding.myRv.adapter = chatAdapter

        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // The reference exists
                    getMessageList()
                } else {
                    // The reference does not exist
                    reference =
                        databse.getReference("chats").child(user.uid + MySharedPreference.user?.uid)
                    getMessageList()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.edtMessage.addTextChangedListener {
            if (it != null) {
                if (it.isEmpty()) {
                    binding.btnSend.visibility = View.GONE
                } else {
                    binding.btnSend.visibility = View.VISIBLE
                }
            }
        }

        binding.btnSend.setOnClickListener {
            val edtMessage = binding.edtMessage.text.toString().trim()
            if (edtMessage.isNotEmpty()) {
                sendMessage(edtMessage, user)
            }

        }

    }
    private fun sendMessage(edtMessage: String, user: User) {
        val USER = MySharedPreference.user
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                binding.btnSend.isEnabled = false
            }
            val messageText = MessageText()
            val key = SimpleDateFormat("dd MM yyyy  HH mm ss SSS", Locale.ENGLISH).format(Date())
            messageText.id = key
            messageText.date = SimpleDateFormat("d MMMM HH:mm", Locale.ENGLISH).format(Date())
            messageText.message = edtMessage
            messageText.senderId = USER?.uid
            messageText.receiverId = user.uid
            messageText.statusRead = if (USER?.uid === user.uid) "true" else "false"
            messageText.positon = chatAdapter.itemCount.toString()


            reference.child(key).setValue(messageText)

            withContext(Dispatchers.Main) {
                binding.edtMessage.text.clear()
                binding.btnSend.isEnabled = true
            }


        }


    }

    private fun getMessageList() {
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                chatAdapter.messages.add(
                    snapshot.getValue(MessageText::class.java)!!
                )
                chatAdapter.notifyItemInserted(chatAdapter.itemCount - 1)
                binding.myRv.scrollToPosition(chatAdapter.itemCount - 1)
                if (binding.progressBar.visibility == View.VISIBLE) {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

    }


}