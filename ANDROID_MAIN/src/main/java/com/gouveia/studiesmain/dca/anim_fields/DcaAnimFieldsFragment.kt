package com.gouveia.studiesmain.dca.anim_fields

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaAnimFieldsBinding
import com.gouveia.studiesmain.utils.extensions.*

class DcaAnimFieldsFragment : Fragment(R.layout.fragment_dca_anim_fields) {

    private val binding by viewBinding(FragmentDcaAnimFieldsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cpfButton.setOnClickListener {
            // SIMULACAO DE VERIFICACÃO SIMPLES PARA FINS DIDÁTICOS
            if (binding.cpf.text.toString().length == 11) {
                hideKeyboard()
                showToast("CPF válido!")
                binding.cpf.setText("")
            } else {
                hideKeyboard()
                vibrate(500L)
                binding.cpf.setText("")
                // faz o shake e mostra o toast.
                binding.cpfLayout.shake { showToast("CPF não existe!") }
            }
        }

    }

}