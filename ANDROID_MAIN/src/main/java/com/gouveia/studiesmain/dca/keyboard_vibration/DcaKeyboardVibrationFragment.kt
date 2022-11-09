package com.gouveia.studiesmain.dca.keyboard_vibration

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.utils.extensions.hideKeyboard
import com.gouveia.studiesmain.utils.extensions.showKeyboard
import com.gouveia.studiesmain.utils.extensions.vibrate
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaKeyboardVibrationBinding

class DcaKeyboardVibrationFragment : Fragment(R.layout.fragment_dca_keyboard_vibration) {

    private lateinit var binding: FragmentDcaKeyboardVibrationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDcaKeyboardVibrationBinding.bind(view)

        with(binding) {
            dcaHideKeyboardButton.setOnClickListener { hideKeyboard() }
            dcaShowKeyboardButton.setOnClickListener { showKeyboard() }
            dcaVibrateButton.setOnClickListener { vibrate(1000) }
        }

    }

}