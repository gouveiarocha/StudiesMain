package com.gouveia.studiesmain.dca.start_for_result

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.gouveia.studiesmain.dca.start_for_result.DcaResultFragment.Companion.KEY_SELECTED_IMAGE_URI
import com.gouveia.studiesmain.utils.extensions.navTo
import com.gouveia.studiesmain.utils.extensions.shouldRequestPermission
import com.gouveia.studiesmain.utils.extensions.showToast
import com.gouveia.studiesmain.utils.others.WayConstans
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaRequestResultBinding

class DcaRequestResultFragment : Fragment(R.layout.fragment_dca_request_result) {

    private lateinit var binding: FragmentDcaRequestResultBinding

    // A CLASSE QUE RECEBE ARGUMENTOS É A CLASSE QUE DISPONIBILIZA AS KEYS
    companion object {
        const val KEY_CHOOSER_REQUEST = "key_chooser_result"
        const val KEY_CHOOSER_BACK_BUTTON = "key_chooser_window"
    }

    // LAUNCHER PARA SELEÇÃO DA IMAGEM
    private lateinit var fileChooserLauncher: ActivityResultLauncher<String>

    // LAUNCHER PARA REQUISIÇÃO DAS PERMISSÕES
    private lateinit var requestPermissionsLauncher: ActivityResultLauncher<Array<String>>

    private val fileChooserPermissions = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logWay("onCreate $this")

        // CONFIGURA LAUNCHER PARA SELEÇÃO DA IMAGEM
        fileChooserLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
                logWay("init config fileChooserLauncher")
                // O GetContent() solicita algum conteúdo. Nesse caso uma img, então nosso retorno é uma uri.
                // Se nossa uri não for nula, navegamos para a tela de resultado, passando a uri.
                uri?.let {
                    navTo(
                        R.id.dcaResultFragment,
                        bundleOf(Pair(KEY_SELECTED_IMAGE_URI, uri.toString()))
                    )
                }
                // CASO ELE NÃO NAVEGUE, ELE RETORNA PARA TELA QUE LANCOU A SOLICITACÃO
            }

        // CONFIGURA LAUNCHER PARA REQUISIÇÃO DAS PERMISSÕES
        requestPermissionsLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionResult ->
                logWay("init config requestPermissionsLauncher")
                val permissionsIdentified =
                    permissionResult.all { it.key in fileChooserPermissions }
                val permissionsGrant = permissionResult.all { it.value }
                if (permissionsIdentified && permissionsGrant) {
                    // VAMOS LANCAR UMA ACTIVITY PARA OBTER O RESULTADO
                    // o input aqui serve para identificar o tipo de conteúdo que queremos. Consultar Documentação para outras opções.
                    logWay("disparando fileChooserLauncher no requestPermissionsLauncher")
                    fileChooserLauncher.launch("image/*")
                } else {
                    val deniedPermissions = permissionResult.map { permission ->
                        if (!permission.value) permission.key else ""
                    }.filter { it.isNotEmpty() }.toList()
                    showWhyPermissionsAreNeeded(deniedPermissions)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logWay("onViewCreated $this")
        binding = FragmentDcaRequestResultBinding.bind(view)

        // COMO OBTER RESULTADO DA TELA QUE FOI CHAMADA
        setFragmentResultListener(KEY_CHOOSER_REQUEST) { requestKey, result ->
            logWay("setFragmentResultListener")
            showToast(requestKey)
            showToast(result.getString(KEY_CHOOSER_BACK_BUTTON) ?: "")
        }

        binding.dcaRequestResultButton.setOnClickListener {
            if (shouldRequestPermission(fileChooserPermissions)) {
                requestPermissionsLauncher.launch(fileChooserPermissions)
                logWay("disparando requestPermissionsLauncher click dcaRequestResultButton")
            } else {
                // SE JA TENHO A PERMISSÃO, LANCAMOS A ACTIVITY DIRETAMENTE
                logWay("disparando fileChooserLauncher click dcaRequestResultButton")
                fileChooserLauncher.launch("image/*")
            }
        }

    }

    private fun showWhyPermissionsAreNeeded(deniedPermissions: List<String>) {
        logWay("showWhyPermissionsAreNeeded")
        val msg = StringBuilder()
        msg.append("READ_EXTERNAL_STORAGE \n")
        msg.append("WRITE_EXTERNAL_STORAGE \n")
        showToast("Você precisa nos conceder as permissões:\n$msg")
    }

    private fun logWay(msg: String) {
        Log.i(WayConstans.Dca.REQUEST_RESULT, msg)
    }

}