package com.gouveia.studiesmain.gdc.core.workmanager.view

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcSelectImageBinding
import com.gouveia.studiesmain.gdc.core.workmanager.utils.KEY_IMAGE_URI
import com.gouveia.studiesmain.utils.extensions.navTo

class GdcSelectImageFragment : Fragment(R.layout.fragment_gdc_select_image) {

    companion object {
        private const val KEY_PERMISSIONS_GRANTED = "KEY_PERMISSIONS_GRANTED"
        private const val KEY_PERMISSIONS_REQUEST_COUNT = "KEY_PERMISSIONS_REQUEST_COUNT"
        private const val MAX_NUMBER_REQUEST_PERMISSIONS = 2
    }

    private lateinit var binding: FragmentGdcSelectImageBinding
    private lateinit var launcher: ActivityResultLauncher<String>

    private var permissionRequestCount: Int = 0
    private var userHasPermission = false
    private val permissions = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configuramos nosso launcher -> Deve ser definido no onCreate ou onAttach
        launcher = registerForActivityResult(ActivityResultContracts.GetContent()) {
            handleImageRequestResult(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcSelectImageBinding.bind(view)

        // Recuperando o ultimo estado caso o usuário rotacione o aparelho
        savedInstanceState?.let {
            permissionRequestCount = it.getInt(KEY_PERMISSIONS_REQUEST_COUNT, 0)
            userHasPermission = it.getBoolean(KEY_PERMISSIONS_GRANTED, false)
        }

        // Assegura que o usuário tens as permissões necessárias
        requestPermissionsOnlyTwice(userHasPermission)

        // Executa o launch e abre o FileChooser.
        binding.gdcSelectImage.setOnClickListener {
            launcher.launch("image/*")
        }

    }

    private fun requestPermissionsOnlyTwice(hasPermissions: Boolean) {
        if (!hasPermissions) {
            if (permissionRequestCount < MAX_NUMBER_REQUEST_PERMISSIONS) {
                permissionRequestCount += 1
                // NOVA API: PRESTA A ATENCÃO AGORA
                val requestPermissionsLauncher = registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissionsResult ->
                    val permissionsIdentified = permissionsResult.all { it.key in permissions }
                    val permissionsGrant = permissionsResult.all { it.value }
                    if (permissionsIdentified && permissionsGrant) {
                        permissionRequestCount = 0
                        userHasPermission = true
                    }
                }
                if (!userHasPermission) {
                    requestPermissionsLauncher.launch(permissions)
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.set_permissions_in_settings,
                    Toast.LENGTH_LONG
                ).show()
                binding.gdcSelectImage.isEnabled = false
            }
        }
    }

    private fun handleImageRequestResult(uri: Uri?) {
        // Navega para proxima etapa onde exibimos as opções de blur
        navTo(R.id.gdcBlurFragment, bundleOf(Pair(KEY_IMAGE_URI, uri.toString())))
    }

    // SALVA ESTADO ATUAL EM CASO DO USUARIO ROTACIONAR O APARELHO
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_PERMISSIONS_REQUEST_COUNT, permissionRequestCount)
        outState.putBoolean(KEY_PERMISSIONS_GRANTED, userHasPermission)
    }

}