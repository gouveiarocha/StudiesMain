package com.gouveia.studiesmain.gdc

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcHomeBinding
import com.gouveia.studiesmain.utils.extensions.navTo

class GdcHomeFragment : Fragment(R.layout.fragment_gdc_home) {

    private lateinit var binding: FragmentGdcHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcHomeBinding.bind(view)

        // CORE
        binding.gdcToastSnake.setOnClickListener { navTo(R.id.gdcToastSnakeFragment) }
        binding.gdcNotification.setOnClickListener { navTo(R.id.gdcNotificationFragment) }

        // https://youtu.be/5AGWzq9JpYo
        binding.gdcWorkManager.setOnClickListener { navTo(R.id.gdcSelectImageFragment) }

        // USER INTERFACE --->

        // LISTENERS & MATERIAL COMPONENTS - https://youtu.be/qE5lZRSrgxo
        binding.gdcMaterialComponents.setOnClickListener { navTo(R.id.gdcMaterialComponentsFragment) }


    }

}