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
        with(binding) {

            // TOAST AND SNAKE -
            gdcToastSnake.setOnClickListener { navTo(R.id.gdcToastSnakeFragment) }
            gdcNotification.setOnClickListener { navTo(R.id.gdcNotificationFragment) }

            // WORK MANAGER - https://youtu.be/5AGWzq9JpYo
            gdcWorkManager.setOnClickListener { navTo(R.id.gdcSelectImageFragment) }

        }

        // USER INTERFACE
        with(binding) {

            // LISTENERS & MATERIAL COMPONENTS - https://youtu.be/qE5lZRSrgxo
            gdcMaterialComponents.setOnClickListener { navTo(R.id.gdcMaterialComponentsFragment) }

            // INTERACTIVE ANDROID STUDIO UI - https://youtu.be/XBUbvKczRRI
            binding.gdcInteractiveUi.setOnClickListener { navTo(R.id.gdcInteractiveUiFragment) }

            // NAVEGAÇÃO ACTIVITIES/FRAGMENTS E INTENTS - https://youtu.be/5gqNUeNi9es
            binding.gdcActivitiesIntents.setOnClickListener { navTo(R.id.gdcSendFragment) }

        }

    }

}