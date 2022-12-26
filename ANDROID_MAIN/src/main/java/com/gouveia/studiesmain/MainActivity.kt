package com.gouveia.studiesmain

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.gouveia.studiesmain.databinding.ActivityMainBinding
import com.gouveia.studiesmain.utils.extensions.preventScreenshotsAndRecentAppThumbnails

// SINGLE PAGE APPLICATION - NESSE PROJETO, VAMOS TENTAR USAR APENAS UMA ACTIVITY.
// VANTAGEM: VOCÊ FAZ CONFIUGRAÇÕES UMA ÚNICA VEZ EM UM LUGAR CENTRAL!

class MainActivity : AppCompatActivity() {

    // BOTTOM NAVIGATION VIEW: https://youtu.be/1mG3-I8bof0
    // 1- ADICIONAR O BottomNavigationView NO LAYOUT E CRIAR MENU (bottom_navigatio_menu)
    // 2- DEFINIR BINDING E OBTER NAVCONTROLLER
    // 3- FAZER O SETUP DO NAVIGATION VIEW USANDO O NAVIGATION GRAPH
    private lateinit var binding: ActivityMainBinding
    private val navController by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!.findNavController()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // OFUSCAMENTO DE TELA
        // Colocando aqui no onCreate da activity, a ofuscação vai se extender para todas as telas
        // vinculadas a essa activity, inclusive os fragments.
        preventScreenshotsAndRecentAppThumbnails()
        // --- FIM OFUSCAMENTO DE TELA

        // CARREGAR FRAGMENT NO CONTAINER. (POC Movies)
        // No ex. abaixo, criamos um Singleton no Fragment para disponibilizar sua instancia.
        // OBS LEGADO: Comentado pois estamos usando o nav_graph para realizar essa transição.
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, PpsMoviesFragment.newInstance())
//            .commitNow()
        // --- FIM CARREGAR NO CONTAINER

        // SPLASHSCREEN ANDROID 12: https://youtu.be/cMXE8PN-qIc
        // Atenção: No vídeo é descrito um bug e como resolve-lo, porém não funcionou aqui no
        // projeto. Deixei o theme do Manifest default para não executar a SplashScreen e podemos
        // revisitar essa implementação no futuro para resolver esse bug.
        // 1- DEFINIFIR DEPENDENCIA NO BUILD.GRADLE
        // 2- CRIAR LOGO, DEFINIR O THEME E SETAR NO MANIFEST
        // 3- ALTERAR O DELAY(SE NECESSÁRIO)
        // 4- ATENÇÃO AO BUG DESCRITO NO FINAL DO VIDEO
        installSplashScreen()

        //Trecho de código está aqui devido necessidade para resolução de bug relacioado a SplashScreen...
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Alterar o tempo de delay da SplashScreen ->
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                val delay = 500L // MAXIMO POSSIVEL PELA API 1s ou 1000L ANDROID 12
                override fun onPreDraw(): Boolean {
                    Thread.sleep(delay)
                    content.viewTreeObserver.removeOnPreDrawListener(this)
                    return true
                }
            }
        )
        // --- FIM SPLASHSCREEN

        // SETUP DO BOTTOM NAVIGATION VIEW: https://youtu.be/1mG3-I8bof0
        setupBottomNavigation()

        // SETUP DA TOOLBAR
        setupToolbar()

    }

    private fun setupBottomNavigation() {
        with(binding.bottomNavigation) { setupWithNavController(navController) }
    }

    // COMO CRIAR UMA TOP ACTION BAR COM NAV CONTROLLER: https://youtu.be/RbOCBzHIwSw
    // 1- ADICIONAR A Toolbar NO LAYOUT E CONFIGURAR O THEME PARA NÃO OCORRER ERRO ABAIXO
    // 2- CONFIGURAR PARA PEGAR OS TITULOS DO NAV_GRAPH
    // 3- FAZER O SETUP
    private fun setupToolbar() {
        // Erro mencionado: Caused by: java.lang.IllegalStateException: This Activity already has an
        // action barsupplied by the window decor. Do not request Window.FEATURE_SUPPORT_ACTION_BAR
        // and set windowActionBar to false in your theme to use a Toolbar instead.
        setSupportActionBar(binding.mainToolbar)
        supportActionBar?.apply { setupActionBarWithNavController(navController) }
        //Configura o voltar da toolbar ->
        binding.mainToolbar.setNavigationOnClickListener { navController.navigateUp() }
    }

    fun setToolbarNavigationIcon(icon: Drawable?) {
        binding.mainToolbar.navigationIcon = icon
    }

    fun setToolbarNavigationTitle(title: String?) {
        binding.mainToolbar.title = title
    }

}