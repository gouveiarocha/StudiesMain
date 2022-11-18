package com.gouveia.studiesmain.gdc.user_interface.material_components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcProductGridBinding
import com.gouveia.studiesmain.utils.extensions.viewBinding

class GdcProductGridFragment : Fragment(R.layout.fragment_gdc_product_grid) {

    private val binding by viewBinding (FragmentGdcProductGridBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}