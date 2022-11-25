package com.gouveia.studiesmain.pps.pocs.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentPpsMoviesBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PpsMoviesFragment : Fragment(R.layout.fragment_pps_movies) {

//    // Singleton para retornar instancia do Fragment.
//    companion object {
//        fun newInstance() = PpsMoviesFragment()
//    }

//    // ViewModel Legado.
//    private lateinit var viewModel: PpsMoviesViewModel

    // ViewModel com Koin e dependencias já injetadas(e passando parametros)
    private val viewModel: PpsMoviesViewModel by viewModel {
        parametersOf(findNavController())
    }

    private var _binding: FragmentPpsMoviesBinding? = null
    private val binding: FragmentPpsMoviesBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPpsMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        // Legado. Factory para retornar nossa ViewModel. Não mais necessário com o uso do Koin.
//        viewModel = ViewModelProvider(
//            this,
//            PpsMoviesViewModel.ViewModelFactory(MainRepository())
//        ).get(PpsMoviesViewModel::class.java)

        setupObserve()

        viewModel.getMoviesJavaThread()
        viewModel.getMoviesCoroutines()

    }

    private fun setupObserve() {

        viewModel.moviesJavaThreadLiveData.observe(viewLifecycleOwner) { movies ->
            binding.txtMoviesJavaThread.text = movies[0].title
        }

        viewModel.moviesCoroutineLiveData.observe(viewLifecycleOwner) { movies ->
            binding.txtMoviesCoroutine.text = movies.map {
                "${it.id} - ${it.title}"
            }.toString()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}