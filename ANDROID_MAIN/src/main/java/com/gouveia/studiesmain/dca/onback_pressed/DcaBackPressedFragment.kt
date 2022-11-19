package com.gouveia.studiesmain.dca.onback_pressed

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaBackPressedBinding
import com.gouveia.studiesmain.utils.extensions.showToast
import com.gouveia.studiesmain.utils.extensions.viewBinding

// 1- COMO REAGIR AO BOTÃO DE BACK DENTRO EM UM FRAGMENT
// 2- COMO IMPLEMENTAR UMA INTERFACE ANONIMA COM KOTLIN

class DcaBackPressedFragment : Fragment(R.layout.fragment_dca_back_pressed) {

    private val binding by viewBinding(FragmentDcaBackPressedBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        // Ex. Convencional ->
//        requireActivity().onBackPressedDispatcher.addCallback(
//            viewLifecycleOwner,
//            object : OnBackPressedCallback(true) {
//                override fun handleOnBackPressed() {
//                    binding.cpfEntry.text?.clear()
//                    showToast("PRESSIONEI O BACK")
//                    findNavController().navigateUp()
//                }
//            })

        // Ex. Com Interface Anonima ->
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            binding.cpfEntry.text?.clear()
            showToast("PRESSIONEI O BACK")
            findNavController().navigateUp()
        }

    }

}