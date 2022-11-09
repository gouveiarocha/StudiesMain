package com.gouveia.studiesmain.gdc.core.toast

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcToastSnakeBinding
import com.gouveia.studiesmain.utils.extensions.showToast

class GdcToastSnakeFragment : Fragment(R.layout.fragment_gdc_toast_snake) {

    private lateinit var binding: FragmentGdcToastSnakeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcToastSnakeBinding.bind(view)

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED

        // Toast Notification - Usando modo convencional.
        binding.toast.setOnClickListener {
            val toastText = "I am TOAST!"
            Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
            //toast(msg) //exemplo usando extensions.
        }

        // Snackbar - Usando modo convencional.
        binding.snake.setOnClickListener {
            val snackText = "I am SNACK!"
            Snackbar.make(view, snackText, Snackbar.LENGTH_SHORT).show()
            //snake(view, snackText) //exemplo usando extensions.
        }

        // Snackbar com Ação
        binding.snakeAction.setOnClickListener {
            Snackbar.make(view, "Snack test with Action...", Snackbar.LENGTH_SHORT)
                .setAction("OK") { showToast("OK Clicked...") }
                .show()
        }

    }

}