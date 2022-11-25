package com.gouveia.studiesmain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gouveia.studiesmain.pps.pocs.movies.di.pocMoviesModule
import com.gouveia.studiesmain.utils.extensions.preventScreenshotsAndRecentAppThumbnails
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

// SINGLE PAGE APPLICATION - NESSE PROJETO, VAMOS TENTAR USAR APENAS UMA ACTIVITY.
// VANTAGEM: VOCÊ FAZ A CONFIGURACÃO UMA ÚNICA VEZ EM UM LUGAR CENTRAL!

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // OFUSCAMENTO DE TELA
        // Colocando aqui no onCreate da activity, a ofuscação vai se extender para todas as telas
        // vinculadas a essa activity, inclusive os fragments.
        preventScreenshotsAndRecentAppThumbnails()

//        // CARREGAR FRAGMENT NO CONTAINER. (POC Movies)
//        // No ex. abaixo, criamos um Singleton no Fragment para disponibilizar sua instancia.
//        // OBS LEGADO: Comentado pois estamos usando o nav_graph para realizar essa transição.
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.container, PpsMoviesFragment.newInstance())
//            .commitNow()

    }

}