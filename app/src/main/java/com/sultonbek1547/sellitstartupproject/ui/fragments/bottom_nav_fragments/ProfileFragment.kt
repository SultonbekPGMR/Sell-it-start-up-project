package com.sultonbek1547.sellitstartupproject.ui.fragments.bottom_nav_fragments

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentProfileBinding
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.ui.LogInActivity
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference


class ProfileFragment : Fragment() {


    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        MySharedPreference.init(requireContext())
        initViews(MySharedPreference.user)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initViews(user: User?) {
        binding.apply {
            Picasso.get().load(user?.imageLink).into(userImage)
            tvUserName.text = "Salom ${user?.name}!"
            tvUserId.text = "ID: ${user?.uid}"
        }
        binding.apply {
            btnPost.setOnClickListener {
                findNavController().navigate(R.id.postFragment)
            }

            btnEditName.setOnClickListener {
                findNavController().navigate(R.id.userNameEditingFragment, bundleOf("user" to user))
            }


            val clipboardManager =
                requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            btnCopyId.setOnClickListener {
                clipboardManager.setPrimaryClip(
                    ClipData.newPlainText("ID", user?.uid)
                )
            }
            val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("sultonbekpgmr@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Got a problem with the app? Let us help you out!")
            }
            btnHelp.setOnClickListener {
                startActivity(Intent.createChooser(emailIntent, "Send Email"))
            }


            btnAboutApp.setOnClickListener {
                findNavController().navigate(R.id.aboutAppFragment)
            }


            btnExit.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Diqqat")
                builder.setMessage("Tizimdan chiqmoqchimisiz?")
                builder.setPositiveButton(
                    "Xa"
                ) { _, _ ->
                    MySharedPreference.isUserAuthenticated = false
                    val intent = Intent(requireContext(), LogInActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK // this code provide not to add activity to history
                    startActivity(intent)                }

                builder.setNegativeButton(
                    "Yo'q"
                ) { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }
        }

    }

}