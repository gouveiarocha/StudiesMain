package com.gouveia.studiesmain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

// REF: https://youtu.be/zu9MOl95LKs
// 1- DefaultExceptionHandler - PARA QUE SERVE? - Serve para possibilitar que você execute alguma
// ação antes de lançar qualquer exceção de runtime (que não devemos tratar) ou não prevista(catch)
// na aplicação - Ex de ações: Limpar Cookies, Limpar Cache, Resetar Status de Alguma API, ETC... A
// depender da regra e necessidade.
// 2- USANDO NA PRÁTICA -> Vamos implementar na MainApplication(primeira aplicação que roda),
// a idéia é defini-lo alí para que ele intercepte qualquer excessão que o sistema venha a lançar.

// IMPLEMENTAÇÃO DE REFERENCIA
class ClearableCoroutineScope(context: CoroutineContext) : CoroutineScope {

    private var onViewDetachJob = Job()

    //agrupa/combina coroutines
    override val coroutineContext: CoroutineContext = context + onViewDetachJob

    fun clearScope() {
        onViewDetachJob.cancel()
    }

}