package com.gouveia.studiesmain.utils.providers

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

// CLASSE PARA PROVER ESCOPOS DE COROUTINES

open class CoroutineContextProvider(
    open val ui: CoroutineContext = Dispatchers.Main,
    open val io: CoroutineContext = Dispatchers.IO
)