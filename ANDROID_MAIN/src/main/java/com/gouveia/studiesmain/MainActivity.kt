package com.gouveia.studiesmain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gouveia.studiesmain.databinding.ActivityMainBinding
import com.gouveia.studiesmain.pps.pocs.movies.di.pocMoviesModule
import com.gouveia.studiesmain.utils.extensions.preventScreenshotsAndRecentAppThumbnails
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// SINGLE PAGE APPLICATION - NESSE PROJETO, VAMOS TENTAR USAR APENAS UMA ACTIVITY.
// VANTAGEM: VOCÊ FAZ CONFIUGRAÇÕES UMA ÚNICA VEZ EM UM LUGAR CENTRAL!

class MainActivity : AppCompatActivity(R.layout.activity_main){

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

    }

}