package com.gouveia.studiesmain.gdc.core.workmanager.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import com.gouveia.studiesmain.gdc.core.workmanager.utils.IMAGE_MANIPULATION_WORK_NAME
import com.gouveia.studiesmain.gdc.core.workmanager.utils.KEY_IMAGE_URI
import com.gouveia.studiesmain.gdc.core.workmanager.utils.TAG_OUTPUT
import com.gouveia.studiesmain.gdc.core.workmanager.workers.BlurWorker
import com.gouveia.studiesmain.gdc.core.workmanager.workers.CleanupWorker
import com.gouveia.studiesmain.gdc.core.workmanager.workers.SaveImageToFileWorker

// MODELO QUE RETEM AS INFORMAÇÕES E FAZ A MEDIAÇÃO ENTRE WORKERS E OBSERVA OS ESTADOS
class BlurViewModel(application: Application) : AndroidViewModel(application) {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)
    internal val outputWorkInfos: LiveData<List<WorkInfo>>

    init {
        // Adquire um live data do workmanager, para poder observar as mudanças de estado na view
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }

    internal fun cancelWork() = workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)

    // CRIA O INPUTDATA PARA O WORKER
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString(KEY_IMAGE_URI, imageUri.toString())
        }
        return builder.build()
    }

    /**
     * Cria uma requisição de trabalho para liberar espaço, realizar o blur e salvar a imagem
     * @param blurLevel nivel do blur como reflexo da seleção do usuario nos radio buttons.
     */
    internal fun applyBlur(blurLevel: Int) {
        // 1) Adiciona o worker de cleanup para limpar arquivos temporarios
        var continuation = workManager
            .beginUniqueWork(
                IMAGE_MANIPULATION_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequest.from(CleanupWorker::class.java)
            )

        // 2) Adiciona tarefa de blur a quantidade de vezes requisitada
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Criar um pre-requisito(constraint) de carregamento de bateria
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        // 3) Criar terceira tarefa agora para salvar o arquivo mas so quando estiver carregando
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
            .setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .setInputData(createInputDataForUri())
            .build()

        continuation = continuation.then(save)

        // Por ultimo coloca tudo em uma fila sequencial e inicia o trabalho
        continuation.enqueue()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) Uri.parse(uriString) else null
    }

    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }

}