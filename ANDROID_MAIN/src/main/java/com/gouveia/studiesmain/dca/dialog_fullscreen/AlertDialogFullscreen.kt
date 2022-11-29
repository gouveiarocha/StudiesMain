package com.gouveia.studiesmain.dca.dialog_fullscreen

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.AlertDialogFullscreenBinding
import com.gouveia.studiesmain.utils.extensions.setVisible
import com.gouveia.studiesmain.utils.extensions.viewBinding

// IMPORTANTE: A CLASSE DEVE EXTENDER DE DialogFragment.

class AlertDialogFullscreen(
    val title: String = "",
    val message: String = "",
    val positiveLabel: String = "",
    val positiveAction: () -> Unit = {},
    val negativeLabel: String? = null,
    val negativeAction: () -> Unit = {},
    val dismissAction: () -> Unit = {},
) : DialogFragment(R.layout.alert_dialog_fullscreen) {

    private val binding by viewBinding(AlertDialogFullscreenBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializa os elementos de view e ação de click.
        with(binding) {
            header.text = title
            body.text = message
            btnPositive.text = positiveLabel
            btnPositive.setOnClickListener {
                positiveAction()
            }
            btnNegative.text = negativeLabel
            btnNegative.setVisible(negativeLabel != null)
            btnNegative.setOnClickListener {
                dismiss()
                negativeAction()
            }
        }

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissAction()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): android.app.Dialog {
        // Seta o Style para FullScreen.
        setStyle(STYLE_NO_FRAME, R.style.Theme_StudiesMain_Fullscreen)
        return super.onCreateDialog(savedInstanceState)
    }
}