package com.gouveia.studiesmain.gdc.user_interface.interactive_ui

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcInteractiveUiBinding

class GdcInteractiveUiFragment : Fragment(R.layout.fragment_gdc_interactive_ui) {

    private lateinit var binding: FragmentGdcInteractiveUiBinding
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcInteractiveUiBinding.bind(view)

        binding.showCount.text = count.toString()
        binding.btnToast.setOnClickListener { showToast() }
        binding.btnCount.setOnClickListener { countUp(); showCounter() }

    }

    private fun showToast() =
        Toast.makeText(requireContext(), R.string.toast_message, Toast.LENGTH_SHORT).show()

    private fun countUp() = count++
    private fun showCounter() {
        binding.showCount.text = count.toString()
    }

}