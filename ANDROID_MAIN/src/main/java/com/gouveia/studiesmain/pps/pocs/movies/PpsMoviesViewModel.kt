package com.gouveia.studiesmain.pps.pocs.movies

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import com.gouveia.studiesmain.pps.pocs.movies.infra.PocMoviesRepository
import com.gouveia.studiesmain.pps.pocs.movies.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PpsMoviesViewModel(
    private val repository: PocMoviesRepository,
    private val navController: NavController
) : BaseViewModel() {

    val moviesJavaThreadLiveData = MutableLiveData<List<Movie>>()
    val moviesCoroutineLiveData = MutableLiveData<List<Movie>>()

    fun getMoviesJavaThread() {
        repository.getMoviesJavaThread { movies ->
            moviesJavaThreadLiveData.postValue(movies)
        }
    }

    fun getMoviesCoroutines() {
        launch {
            val movies = withContext(Dispatchers.Default) {
                repository.getMoviesCoroutines()
            }
            moviesCoroutineLiveData.value = movies
        }
    }

//    // Classe Factory para retornar instancia do ViewModel. Não mais necessário com uso do Koin.
//    class ViewModelFactory(private val repository: PocMoviesRepository) : ViewModelProvider.Factory {
//        override fun <T : ViewModel> create(modelClass: Class<T>): T {
//            return PpsMoviesViewModel(repository) as T
//        }
//    }

}