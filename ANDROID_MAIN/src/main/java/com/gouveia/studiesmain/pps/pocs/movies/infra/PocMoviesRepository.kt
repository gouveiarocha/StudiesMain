package com.gouveia.studiesmain.pps.pocs.movies.infra

import com.gouveia.studiesmain.pps.pocs.movies.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface PocMoviesIRepository {
    fun getMoviesJavaThread(callback: (movies: List<Movie>) -> Unit)
    suspend fun getMoviesCoroutines(): List<Movie>
}

class PocMoviesRepository : PocMoviesIRepository {

    override fun getMoviesJavaThread(callback: (movies: List<Movie>) -> Unit) {
        // Simular Nova Thread do Java
        Thread(Runnable {
            // 3 segundos...
            Thread.sleep(3000)
            callback.invoke(
                listOf(
                    Movie(1, "Filme Thread JAVA"),
                    Movie(2, "Titulo 2")
                )
            )
        }).start()
    }

    override suspend fun getMoviesCoroutines(): List<Movie> {
        return withContext(Dispatchers.Default) {
            delay(3000L)
            listOf(
                Movie(1, "Filme Thread Coroutine"),
                Movie(2, "Titulo 2")
            )
        }
    }

}