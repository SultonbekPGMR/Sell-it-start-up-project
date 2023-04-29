package com.sultonbek1547.sellitstartupproject.ui.fragments.profilefragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sultonbek1547.sellitstartupproject.R
import com.sultonbek1547.sellitstartupproject.databinding.FragmentUserNameEditingBinding
import com.sultonbek1547.sellitstartupproject.models.User
import com.sultonbek1547.sellitstartupproject.utils.MySharedPreference
import com.sultonbek1547.sellitstartupproject.utils.showToast

class UserNameEditingFragment : Fragment() {
    private lateinit var binding: FragmentUserNameEditingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentUserNameEditingBinding.inflate(layoutInflater, container, false)
        MySharedPreference.init(requireContext())
        val user = arguments?.getSerializable("user") as User
        binding.edtUserName.setText(user.name)
        binding.edtUserName.addTextChangedListener { it1 ->
            it1?.let {
                if (it.toString().isNotEmpty()) {
                    switchBtnBackground(1)
                    return@addTextChangedListener
                }
                switchBtnBackground(0)

            }
        }

        binding.btnSave.setOnClickListener {
            if (it.isEnabled) {
                saveNewName(user, binding.edtUserName.text.toString().trim())
            }
        }


        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun saveNewName(user: User, userName: String) {
        user.name = userName
        MySharedPreference.user = user
        findNavController().popBackStack()
    }

    @SuppressLint("Recycle")
    private fun switchBtnBackground(key: Int) {
        binding.btnSave.apply {
            if (key == 1) {
                isEnabled = true
                background = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.selectable_text_background_selected
                )
                setTextColor(resources.getColor(R.color.white))
                foreground =
                    context.obtainStyledAttributes(intArrayOf(android.R.attr.selectableItemBackground))
                        .getDrawable(0)

                return
            }

            isEnabled = false
            background = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.selectable_text_background
            )
            setTextColor(resources.getColor(R.color.light_blue))
            foreground = null
        }

    }

}