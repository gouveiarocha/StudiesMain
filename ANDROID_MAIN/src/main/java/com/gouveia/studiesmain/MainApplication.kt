package com.gouveia.studiesmain

import android.app.Application
import com.gouveia.studiesmain.BuildConfig
import com.gouveia.studiesmain.pps.pocs.movies.di.pocMoviesModule
import com.gouveia.studiesmain.utils.others.CustomLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

// INSTRUÇÕES AULA LOG COM TIMBER
// 1) Inserir dependência no build.gradle
// 2) Criar a classe de log customizada para sua empresa usar me prod
// 3) Criar sua classe de application para inicializar seus timbers
// 4) Adicionar sua classe de application no manifest

// +-----------------------------------------------------------------+
// | Aula de distribuição de apps e logs atraves de Appcenter        |
// +-----------------------------------------------------------------+
// Log Crashes over AppCenter
// import com.microsoft.appcenter.AppCenter
// import com.microsoft.appcenter.crashes.Crashes

// +-----------------------------------------------------------------+
// | Aula de injeção de dependência com Koin                         |
// +-----------------------------------------------------------------+
// Init Dependency Injection Koin
// import org.koin.android.ext.koin.androidContext
// import org.koin.android.ext.koin.androidLogger
// import org.koin.core.logger.Level

// NECESSÁRIO DEFINIR CLASSE NO MANIFEST
class MainApplication : Application() {

//    // +--------------------------------------------------------------------+
//    // | CONTENT PROVIDER C/ INJECAO DE DEPS: https://youtu.be/XXXXXXXXXXX  |
//    // +--------------------------------------------------------------------+
//    override fun attachBaseContext(base: Context?) {
//        super.attachBaseContext(base)
//        // MOVER INICIALIZACÃO DE INJECAO DE DEPS PARA ESTE MÉTODO, JÁ QUE OS
//        // CONTENT PROVIDERS SÃO INICIALIZADOS ANTES DO "onCreate" DA APPLICATION
//        // setupDependencyInjection()
//    }

    override fun onCreate() {
        super.onCreate()
        setupLogging()
        setupDefaultExceptionHandler()
//        setupDependencyInjection()

        // Koin - Aqui, vamos startar o Koin. (POC Movies)
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(pocMoviesModule)
        }

//        // +-----------------------------------------------------------------+
//        // | COMO MIGRAR SETTINGS P/ APP NOVO: https://youtu.be/HANcH98pU6I  |
//        // +-----------------------------------------------------------------+
//        MigrationUtil(this).migrateOnlyOnce()

    }

// +-----------------------------------------------------------------+
// | Aula de injeção de dependência com Koin                         |
// +-----------------------------------------------------------------+
//    private fun setupDependencyInjection() = initKoin {
//        androidLogger(Level.NONE)
//        androidContext(this@MainApplication)
//        modules(
//            appModule,
//        )
//    }


    // CONFIGURA O LOG - https://youtu.be/rz8O8V4Ho1M
    private fun setupLogging() {
        // Aqui, vamos verificar se estamos com ambiente de debug e usar o Timber default.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            // Caso contrario, vamos usar o nosso CustomLogger para PROD.
            Timber.plant(CustomLogger())

            // SE VOCE OPTAR POR LOGAR CRASHES COM APPCENTER, CONFIGURACÕES NECESSÁRIAS
            // Crashes.setEnabled(true)
            // AppCenter.setLogLevel(Log.ERROR)
            // AppCenter.start(this, BuildConfig.APP_CENTER_ID, Crashes::class.java)

        }
    }

    // CONFIGURA O DefaultExceptionHandler - https://youtu.be/zu9MOl95LKs
    private fun setupDefaultExceptionHandler() {
        //Pega o DefaultUncaughtExceptionHandler para repassar os erros ->
        val existingHandler = Thread.getDefaultUncaughtExceptionHandler()
        //Intercepta os erros ->
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            ClearableCoroutineScope(Dispatchers.Default).launch {
                //Aqui podemos fazer as ações necessárias.
                Timber.v("DefaultExceptionHandler - Clearing Cookies") //LOG de exemplo...
            }
            //Após as ações, lança os erros ao sistema ->
            existingHandler?.uncaughtException(thread, exception)
        }
    }

}