package com.gouveia.studiesmain

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.compose.navArgument
import com.gouveia.studiesmain.databinding.FragmentMainBinding
import com.gouveia.studiesmain.utils.extensions.navTo

class MainFragment : Fragment(R.layout.fragment_main) {

    private lateinit var binding: FragmentMainBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        binding.btnGdc.setOnClickListener { navTo(R.id.gdcHomeFragment) }
        binding.btnDca.setOnClickListener { navTo(R.id.dcaHomeFragment) }

        // ESTUDOS PROPRIOS
        binding.btnPpsSensorManager.setOnClickListener { navTo(R.id.ppsSensor) }
        binding.btnPpsPocMovies.setOnClickListener { navTo(R.id.ppsPocMovies) }

    }

}