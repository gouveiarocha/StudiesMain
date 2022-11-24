package com.gouveia.studiesmain.dca.biometry

import android.os.Bundle
import android.view.View
import androidx.biometric.BiometricPrompt.*
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaBiometryBinding
import com.gouveia.studiesmain.utils.extensions.launchBiometricAuth
import com.gouveia.studiesmain.utils.extensions.showToast
import com.gouveia.studiesmain.utils.extensions.viewBinding

// REFERÊNCIAS OFICIAIS
// https://developer.android.com/jetpack/androidx/releases/biometric
// https://developer.android.com/training/sign-in/biometric-auth

// 1- Adicionar dependência -> "androidx.biometric:biometric-ktx:1.2.0-alpha04"
// 2- Lançar através de uma extensão para obter o biometric prompt
// 3- Usar ele passando os callbacks que deseja

class DcaBiometryFragment : Fragment(R.layout.fragment_dca_biometry) {

    private val binding by viewBinding(FragmentDcaBiometryBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.biometryButton.setOnClickListener {
            launchBiometricAuth(
                "DESBLOQUEAR",
                "USE SUA DIGITAL",
                "CANCELAR",
                confirmationRequired = true,
                null,
                { result ->
                    when (result.authenticationType) {
                        AUTHENTICATION_RESULT_TYPE_BIOMETRIC -> showToast("Sucesso!")
                        AUTHENTICATION_RESULT_TYPE_UNKNOWN -> showToast("Sucesso! (Por meio legado ou desconhecido)")
                        AUTHENTICATION_RESULT_TYPE_DEVICE_CREDENTIAL -> showToast("Sucesso! (Pin, Pattern or Password)")
                    }
                },
                { error, errorMsg ->
                    showToast("$error: $errorMsg")
                })
        }

    }

}