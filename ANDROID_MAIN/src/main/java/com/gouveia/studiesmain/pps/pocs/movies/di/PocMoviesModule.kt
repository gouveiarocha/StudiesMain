package com.gouveia.studiesmain.pps.pocs.movies.di

import androidx.navigation.NavController
import com.gouveia.studiesmain.pps.pocs.movies.PpsMoviesViewModel
import com.gouveia.studiesmain.pps.pocs.movies.infra.PocMoviesRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val pocMoviesModule = module {

    single {
        PocMoviesRepository()
    }

    viewModel { (navController: NavController) ->
        PpsMoviesViewModel(
            repository = get(),
            navController = navController
        )
    }

}

//// PRIMEIRA VARIAÇÃO (DEPENDENCIA ACOPLADA)
//val pocMoviesModule1 = module {
//    // O escopo da viewModel é disponibilizado pelo próprio Koin
//    viewModel {
//        // Declaramos a nossa ViewModel com a dependencia. Assim o repository vive no mesmo ciclo
//        // de vida da viewModel.
//        PpsMoviesViewModel(PocMoviesRepository())
//    }
//}
//
//// SEGUNDA VARIAÇÃO (DEPENDENCIA DESACOPLADA)
//val pocMoviesModule2 = module {
//    // Usando o Factory, deixamos ele fora do escopo da ViewModel e disponivel para ser utilizado
//    // por outras features.
//    factory {
//        PocMoviesRepository()
//    }
//
//    viewModel {
//        // Agora, usando o get() para pegar a dependencia, antes configurada no Factory. Observar
//        // que podemos nomear o parametro para melhor compreensão.
//        PpsMoviesViewModel(
//            repository = get()
//        )
//    }
//}
//
//
//// TERCEIRA VARIAÇÃO (COMPARTILHANDO A INSTANCIA COM SINGLE)
//val pocMoviesModule3 = module {
//    // Usando o Single tb deixamos ele disponivel p outras features, mas compartilhando a instancia,
//    // ou seja, na primeira requisição ele cria a instancia, e nas demais ele entrega a mesma.
//    single {
//        PocMoviesRepository()
//    }
//
//    viewModel {
//        PpsMoviesViewModel(
//            repository = get()
//        )
//    }
//}
//
//// QUARTA VARIAÇÃO (PASSANDO PARAMETRO)
//val pocMoviesModule4 = module {
//    single {
//        PocMoviesRepository()
//    }
//    // Agora, passando um parametro.
//    viewModel { (navController: NavController) ->
//        PpsMoviesViewModel(
//            repository = get(),
//            navController = navController
//        )
//    }
//}