package com.gouveia.studiesmain.dca

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentDcaHomeBinding
import com.gouveia.studiesmain.utils.EmulatorDetector
import com.gouveia.studiesmain.utils.extensions.*

class DcaHomeFragment : Fragment(R.layout.fragment_dca_home) {

    // (ANTES) DO BINDING DELEGATE
    //private lateinit var binding: FragmentDcaHomeBinding

    // (DEPOIS) COM BINDING DELEGATE
    private val binding by viewBinding(FragmentDcaHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // (ANTES) DO BINDING DELEGATE
        //binding = FragmentDcaHomeBinding.bind(view)

        with(binding) {

            //SOLICITAR PERMISSÕES COM API NOVA DA GOOGLE: https://youtu.be/grYUKZDTzVA
            dcaRequestPermission.setOnClickListener { navTo(R.id.dcaRequestPermissionFragment) }

            //LANCAR ACTIVITY, FRAGMENTS E OBTER RESULTADO: https://youtu.be/mhm096S_qrA
            dcaRequestResult.setOnClickListener { navTo(R.id.dcaRequestResultFragment) }

            //ESCONDER TECLADO: https://youtu.be/OzK1fJi9FiQ
            //VIBRAR CELULAR: https://youtu.be/ogxgiaCq_24
            dcaKeyboardVibration.setOnClickListener { navTo(R.id.dcaKeyboardVibrationFragment) }

            //COMO BLOQUEAR / IMPEDIR CAPTURAS DE TELAS: https://youtu.be/7zUdUYiu8Rs
            dcaObfuscation.setOnClickListener { navTo(R.id.dcaObfuscationFragment) }

            //COMO VERIFICAR CONEXÃO COM INTERNET: https://youtu.be/DpyxLwibE0M
            dcaHasInternet.setOnClickListener { showToast(if (hasInternet()) "YES :)" else "NO :(") }

            //COMO CRIAR E GRAVAR VIDEOS COM ANDROID STUDIO: https://youtu.be/1vB46ujfVrA
            dcaCreateVideo.setOnClickListener { showYoutubeVideo("1vB46ujfVrA") }

            //COMO FORÇAR DARK MODE NA WEB VIEW: https://youtu.be/aMuHOlTNL9E
            dcaWebViewDarkMode.setOnClickListener { navTo(R.id.dcaWebViewDarModeFragment) }

            //COMO CRIAR ÍCONES ADAPTÁVEIS E LEGACY: https://youtu.be/FNQ3DQSVd30
            dcaAdaptiveIcons.setOnClickListener { showYoutubeVideo("FNQ3DQSVd30") }

            //COMO VISUALIZAR ERROS COM TIMBER EM PRODUCÃO: https://youtu.be/rz8O8V4Ho1M
            dcaTimber.setOnClickListener { showYoutubeVideo("rz8O8V4Ho1M") }

            //COMO USAR BINDING DELEGATE COM CLASSE CUSTOMIZADA + KOTLIN EXTENSIONS
            dcaBindingDelegate.setOnClickListener { showYoutubeVideo("qivrch6qxQw") }

            //SINGLETON QUE RETORNA SE O APARELHO É OU NÃO UM EMULADOR: https://youtu.be/A14WEDpWjds
            dcaCheckEmulator.setOnClickListener { showToast(if (EmulatorDetector.isEmulator()) "YES" else "NO") }

        }

    }

}