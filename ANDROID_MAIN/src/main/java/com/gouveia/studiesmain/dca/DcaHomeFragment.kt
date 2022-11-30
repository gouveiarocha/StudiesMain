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

            //ANIMACÕES DE ELEMENTOS: https://youtu.be/4WMmin8vnU0
            dcaAnimFields.setOnClickListener { navTo(R.id.dcaAnimFieldsFragment) }

            //COMO PERSISTIR DADOS EM MEMÓRIA COM SP (CHAVE-VALOR): https://youtu.be/XBqY-3MPjkg
            //Aula explana a classe LocalStorage.kt - Foco na classe interna LocalSimpleStorage
            dcaSaveInMemory.setOnClickListener { showYoutubeVideo("XBqY-3MPjkg") }

            //COMO CRIPTOGRAFAR DADOS SENSIVEIS: https://youtu.be/aJqZ38-bZUc
            //Aula explana a classe LocalStorage.kt - Foco na classe interna LocalSecureStorage e suas dependências
            dcaSaveInMemoryCriptography.setOnClickListener { showYoutubeVideo("aJqZ38-bZUc") }

            //COMO REAGIR AO BOTÃO DE BACK EM FRAGMENTS (EX. C\ INTERFACE ANONIMA): https://youtu.be/8pvQ-dTaRGI
            dcaOnbackPressed.setOnClickListener { navTo(R.id.dcaBackPressedFragment) }

            // BIOMETRIA: https://youtu.be/xpN94rgPkds
            dcaBiometry.setOnClickListener { navTo(R.id.dcaBiometryFragment) }

            // DIALOGS CUSTOMIZADOS: https://youtu.be/W8La6tMRv14
            // DIALOG FULLSCREEN: https://youtu.be/DRkS0NWNAvg
            dcaFullscreenDialog.setOnClickListener { navTo(R.id.dcaDialogs) }

        }

    }

}