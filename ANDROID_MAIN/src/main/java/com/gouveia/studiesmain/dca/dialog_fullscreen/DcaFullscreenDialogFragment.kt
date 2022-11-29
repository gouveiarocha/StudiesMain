package com.gouveia.studiesmain.dca.dialog_fullscreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaFullscreenDialogBinding
import com.gouveia.studiesmain.utils.extensions.showFullscreenAlertDialog
import com.gouveia.studiesmain.utils.extensions.showToast
import com.gouveia.studiesmain.utils.extensions.viewBinding

// 1- CRIAR CLASSE\LAYOUT P O DIALOG(AlertDialogFullscreen) E DEFINIR O STYLE PARA FULLSCREEN.
// 2- CRIAR EXTENSION (showFullscreenAlertDialog).
// 3- TESTAR!

class DcaFullscreenDialogFragment() : Fragment(R.layout.fragment_dca_fullscreen_dialog) {

    private val binding by viewBinding(FragmentDcaFullscreenDialogBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDialog.setOnClickListener {
            showFullscreenAlertDialog(
                "DIALOG PERSONALIZADO!!!",
                "TESTE DE MENSAGEM",
                "POSITIVE BUTTON", { showToast("POSITIVE BUTTON") },
                "NEGATIVE BUTTON", { showToast("NEGATIVE BUTTON") }
            )
        }

    }

}