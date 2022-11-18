package com.gouveia.studiesmain.gdc.user_interface.material_components

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcMaterialComponentsBinding
import com.gouveia.studiesmain.utils.extensions.navTo
import com.gouveia.studiesmain.utils.extensions.viewBinding

// NESSE ESTUDO, VAMOS VER COMO FUNCIONA OS LISTENERS E VER COMO O MATERIAL COMPONENTS NOS
// PROPORCIONA ALGUMAS INTERAÇÕES INTERESSANTES.
// A. LISTENERS setOnClickListener e setOnKeyListener
// B. AVISO DE ERRO NO INPUT DO PASSWORD GERENCIADO PELO MATERIAL DESING

class GdcMaterialComponentsFragment : Fragment(R.layout.fragment_gdc_material_components) {

    private val binding by viewBinding(FragmentGdcMaterialComponentsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aqui, um listener que já conhecemos - o setOnClickListener - que reage ao click.
        binding.nextButton.setOnClickListener {
            if (!isPasswordValid(binding.passwordEditText.text.toString())) {
                // Password invalido ->
                // Aqui, o .error é uma interação do Material Components e vai apresentar um erro
                // para o usuario. É preciso ver na prática para compreender melhor.
                binding.passwordTextInput.error = getString(R.string.shr_error_password)
            } else {
                // Password válido -> Define o error null e navega para próxima tela.
                binding.passwordTextInput.error = null
                navTo(R.id.gdcProductGridFragment)
            }
        }

        // Aqui, o listener setOnKeyListener reage a cada caracter inserido ou deletado no campo.
        binding.passwordEditText.setOnKeyListener { _, _, _ ->
            // Essa verificação esta sendo feita a cada caracter digitado ou excluido do campo e
            // quando ele validar c sucesso, define o error null, limpando o erro do Material Components.
            if (isPasswordValid(binding.passwordEditText.text.toString())) {
                binding.passwordTextInput.error = null
            }
            false
        }

        binding.cancelButton.setOnClickListener {
            binding.passwordEditText.setText("")
            binding.username.setText("")
        }

    }

    private fun isPasswordValid(text: String): Boolean = text.length >= 8

}