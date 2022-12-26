package com.gouveia.studiesmain.dca.dialogs

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaDialogsBinding
import com.gouveia.studiesmain.dca.dialogs.bottom_sheet.TicketSelectionFragment
import com.gouveia.studiesmain.utils.extensions.*

// DIALOGS CUSTOMIZADOS USANDO EXTENSIONS.

class DcaDialogsFragment : Fragment(R.layout.fragment_dca_dialogs) {

    private val binding by viewBinding(FragmentDcaDialogsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // Default
            btnDefaultDialog.setOnClickListener {
                showDefaultMaterialAlertDialog(
                    title = "Padrão",
                    message = "Mensagem Padrão",
                    positiveButtonLabel = "Aceitar",
                    positiveButtonClickListener = { showToast("Tudo Certo!") },
                    negativeButtonLabel = "Fechar",
                    negativeButtonClickListener = { showToast("Fechei!") })
            }

            // Semi-Custom -> Nesse, podemos passar o id do layout estático para exibir.
            // No ex. abaixo, testamos os dois layouts 'alert_dialog_custom' e 'alert_dialog_feedback'
            btnCustomizedDialog.setOnClickListener {
                showDefaultMaterialAlertDialogWithCustomStaticContent(
                    positiveButtonLabel = "OK",
                    positiveButtonClickListener = { showToast("OK") },
                    customLayoutId = R.layout.alert_dialog_feedback,
                    customBackgroundId = R.drawable.rounded_border
                )
            }

            // Total-Custom -> Nesse, podemos passar uma view para ser inflada e exibida.
            // Observar que esse método cria e nos retorna o dialog, e aqui fazemos as demais
            // configurações q quisermos, como setar qualquer elemento da view e ações dos botões.
            val customView = layoutInflater.inflate(R.layout.alert_dialog_custom_button, null)
            val dialog = createFullCustomAlertDialog(
                customView = customView,
                customBackgroundId = R.drawable.rounded_border
            )
            with(customView) {
                findViewById<ImageView>(R.id.custom_dialog_icon).setImageResource(R.drawable.ic_error)
                findViewById<TextView>(R.id.custom_dialog_title).text = "Title"
                findViewById<TextView>(R.id.custom_dialog_message).text = "Description"
                findViewById<Button>(R.id.btn_positive_custom).setOnClickListener {
                    dialog.dismiss()
                    showToast("POSITIVE BUTTON")
                }
                findViewById<Button>(R.id.btn_negative_custom).setOnClickListener {
                    dialog.dismiss()
                    showToast("NEGATIVE BUTTON")
                }
            }
            btnFullCustomizedDialog.setOnClickListener { dialog.show() }

            // Fullscreen - Nesse exemplo, o método já retorna uma view construida controlada por
            // uma classe e nela são feitas as ações, inclusive de alteração do tema para fullscreen.
            btnFullscreenDialog.setOnClickListener {
                showFullscreenAlertDialog(
                    "DIALOG PERSONALIZADO!!!",
                    "TESTE DE MENSAGEM",
                    "POSITIVE BUTTON", { showToast("POSITIVE BUTTON") },
                    "NEGATIVE BUTTON", { showToast("NEGATIVE BUTTON") }
                )
            }

            // BOTTOM SHEET DIALOG: https://youtu.be/vku9pMNHT9o
            // 1- CRIAR CLASSE UTILITARIA (TicketUtil)
            // 2- DEFINIR LAYOUT E ITENS A SEREM EXIBIDOS
            // 3- CRIAR A CLASSE DE TELA FLUTUANTES
            btnSheetDialog.setOnClickListener {
                TicketSelectionFragment(true).show(
                    parentFragmentManager,
                    BottomSheetDialogFragment::class.simpleName
                )
            }

        }

    }

}