package com.gouveia.studiesmain.dca.polling_api

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaPollingApiBinding
import com.gouveia.studiesmain.utils.extensions.hasInternet
import com.gouveia.studiesmain.utils.extensions.polling
import com.gouveia.studiesmain.utils.extensions.showToast
import com.gouveia.studiesmain.utils.extensions.viewBinding

// 1- DEFINIR ESCOPOS DE COROUTINES
// 2- DEFINIR EXTENSÃO DE POLLING
// 3- USAR O MÉTODO RECURSIVO NA PRATICA

class DcaPollingApiFragment : Fragment(R.layout.fragment_dca_polling_api) {

    private val binding by viewBinding(FragmentDcaPollingApiBinding::bind)

    // Mock para status de API.
    private var stateSuccess = false
    private var stateError = false
    private var stateHasInternet = false
    private var stateCanceled = false

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSimulateSuccess.setOnClickListener {
            stateSuccess = !stateSuccess
        }

        binding.btnSimulateError.setOnClickListener {
            stateError = !stateError
        }

        binding.btnSimulateNoInternet.setOnClickListener {
            stateHasInternet = !stateHasInternet
        }

        binding.btnSimulateCanceled.setOnClickListener {
            stateCanceled = !stateCanceled
        }

        binding.btnStartPolling.setOnClickListener {
            polling(
                isOffline = { !hasInternet() },
                onOffline = { showToast("Sem Internet") },
                isCompleted = { stateSuccess },
                onCompleted = { showToast("Sucesso") },
                isError = { stateError },
                onError = { showToast("Erro") },
                isCanceled = { stateCanceled },
                onCanceled = { showToast("Polling Cancelado") }
            )
        }
    }

}