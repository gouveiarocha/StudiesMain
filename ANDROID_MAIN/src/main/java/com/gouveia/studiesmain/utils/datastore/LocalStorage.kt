package com.gouveia.studiesmain.utils.datastore

import android.content.SharedPreferences
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties.*
import android.util.Base64
import androidx.core.content.edit
import java.security.Key
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

// LOCAL STORAGE COM SHARED PREFERENCES --->
// 1) DEFINIR INTERFACE COMUM (PROJETOS MULTIPLATAFORMA OU COM BASE COMUM)
// 2) CRIAR NOSSA INSTANCIA PARA PERSISTIR CHAVE-VALOR SIMPLES
// 3) CRIAR UMA NOVA INSTANCIA INTERCAMBIÁVEL PARA PERSISTIR CHAVE-VALOR CRIPTOGRAFADA
// 4) ENTENDER PORQUE ISSO FACILITA NOSSA VIDA E OS TESTES

// INTERFACE --->
// USAR INTERFACE TORNA OS TIPOS DE PERSISTÊNCIAS INTERCAMBIÁVEIS E INJETÁVEIS ATRAVÉS DE KOIN,
// HILT ETC... E DEIXA AS CLASSES MAIS FLEXIVÉIS PARA TESTES UNITÁRIOS
interface StorageType {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
    fun deleteString(key: String)
    fun contains(key: String): Boolean
    fun clear()
}

// EX. DE CLASSES COM ARMAZENAMENTO LOCAL BASEADO EM CHAVE-VALOR SIMPLES
open class LocalSimpleStorage(private val preferences: SharedPreferences) : StorageType {
    override fun putString(key: String, value: String) = preferences.edit { putString(key, value) }
    override fun getString(key: String): String = preferences.getString(key, "") ?: ""
    override fun deleteString(key: String) = preferences.edit { remove(key) }
    override fun contains(key: String): Boolean = preferences.contains(key)
    override fun clear() = preferences.edit().clear().apply()
}

// EX. DE CLASSES COM ARMAZENAMENTO LOCAL BASEADO EM CHAVE-VALOR CRIPTOGRAFADO P\ DADOS SENSIVEIS
open class LocalSecureStorage(private val preferences: SharedPreferences) : StorageType {

    // CONSTANTES
    companion object {
        // IDENTIFICADOR ÚNICO DA SUA SECURE STORE QUE VC MESMO DEFINE
        private const val ANDROID_SECURE_KEY_STORE = "AndroidSecureKeyStore"

        // MODO DE CODIFICACÃO PARA QUANDO USAR O BASE64
        private const val BASE_64_MODE = Base64.NO_WRAP

        // AES: https://www.quora.com/Does-AES-GCM-require-padding
        // AES, ou Advanced Encryption Standard, é uma cifra de bloco que criptografa
        // blocos de dados em 128 bits
        private const val AES_MODE = "AES/GCM/NoPadding"

        // GCM: https://en.wikipedia.org/wiki/Galois/Counter_Mode
        // Na criptografia, o Modo Galois / Contador (GCM) é um modo de operação para cifras de
        // bloco criptográfico de chave simétrica amplamente adotado para seu desempenho
        private const val GCM_TAG_LENGTH = 128

        // TAMANHO DA CHAVE SECURA QUE SERÁ GERADA
        private const val AES_KEY_SIZE = 256
    }

    override fun putString(key: String, value: String) {
        encrypt(value, key, getOrGenerateKey(key))
    }

    override fun getString(key: String): String? {
        return when {
            contains(key) -> decrypt(key, getOrGenerateKey(key))
            else -> null
        }
    }

    override fun deleteString(key: String) {
        // REMOVER ENTRADA DA SECURE STORE
        keyStore.run {
            if (containsAlias(key)) {
                deleteEntry(key)
            }
        }
        // REMOVER ENTRADAS "HUMANAS" DA MEMÓRIA LOCAL
        preferences.edit {
            remove(key)
            remove(initializationVectorKey(key))
        }
    }

    override fun contains(key: String): Boolean {
        // VERIFICA SE PREFERENCIA CONTEM A CHAVE E O IV
        return preferences.contains(key) && preferences.contains(initializationVectorKey(key))
    }

    override fun clear() {
        // REMOVE TODAS AS ENTRADAS DA SECURE STORE
        keyStore.aliases().toList().forEach { deleteString(it) }
    }

    // METODOS UTILITÁRIOS DA CRIPTOGRAFIA --->

    // CRIA UMA INSTANCIA ÚNICA NO MOMENTO QUE ELA FOR USADA PELA PRIMEIRA VEZ
    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(ANDROID_SECURE_KEY_STORE).apply {
            load(null)
        }
    }

    // GERA OU OBTEM UMA CHAVE SIMETRICA PARA CRIPTOGRAFIA
    protected fun getOrGenerateKey(key: String, useForBiometricAuth: Boolean = false): Key {
        return if (!keyStore.containsAlias(key)) {
            // CRIA UMA CHAVE SIMETRICA NOVA
            createSymmetricKey(key, useForBiometricAuth)
        } else {
            // SE JA EXISTE RETORNA A KEY
            keyStore.getKey(key, null)
        }
    }

    // IV: VETOR DE INICIALIZAÇÃO: IDENTIFICADOR ÚNICO COMPARÁVEL COM UMA NONCE
    private fun initializationVectorKey(key: String): String = "iv_$key"

    // VERIFICA SE IV EXISTE, REMOVE BASE64 E RETORNA
    private fun loadExistingInitializationVector(key: String): ByteArray {
        return preferences
            .getString(initializationVectorKey(key), null)
            ?.let { Base64.decode(it, BASE_64_MODE) }
            ?: throw IllegalStateException("initialization vector for $key missing")
    }

    // PEGA A IV, EMBALA EM BASE64 E SALVA NAS PREFERENCIAS COM SUA RESPECTIVA KEY
    private fun storeNewInitializationVector(key: String, iv: ByteArray) {
        Base64.encodeToString(iv, BASE_64_MODE)
            .let { preferences.edit { putString(initializationVectorKey(key), it) } }
    }

    // CRIA SYMMETRICKEY
    private fun createSymmetricKey(key: String, useForBiometricAuth: Boolean = false): Key {
        val symmetricKeyGen = KeyGenerator.getInstance(KEY_ALGORITHM_AES, ANDROID_SECURE_KEY_STORE)
        val spec = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeyGenParameterSpec.Builder(key, PURPOSE_ENCRYPT or PURPOSE_DECRYPT)
                .setKeySize(AES_KEY_SIZE)
                .setBlockModes(BLOCK_MODE_GCM)
                .setEncryptionPaddings(ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .setUserAuthenticationRequired(useForBiometricAuth)
                .apply {
                    // Se for maior que versão 24, adicione esse par. p/ identificar que pode ou
                    // não ser usado para autenticar por/com biometrica também.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        setInvalidatedByBiometricEnrollment(useForBiometricAuth)
                    }
                }
                .build()
        } else {
            // Não previsto durante a aula.
            TODO("VERSION.SDK_INT < M")
        }
        symmetricKeyGen.init(spec)
        return symmetricKeyGen.generateKey()
    }

    // PEGA O MODO DO CIPHER (ENCRIPTA OU DECRIPTA)
    protected fun getCipherByMode(key: String, cypherSymmetricKey: Key, mode: Int): Cipher {
        val cipher = Cipher.getInstance(AES_MODE)
        return when (mode) {
            // DESCRIPTOGRAFA
            Cipher.DECRYPT_MODE -> {
                cipher.apply {
                    init(
                        mode,
                        cypherSymmetricKey,
                        GCMParameterSpec(GCM_TAG_LENGTH, loadExistingInitializationVector(key))
                    )
                }
            }
            // CRIPTOGRAFA
            Cipher.ENCRYPT_MODE -> {
                cipher
                    .apply { init(mode, cypherSymmetricKey, SecureRandom()) }
                    .also { storeNewInitializationVector(key, it.iv) }
            }
            else -> throw IllegalArgumentException("unsupported mode: $mode")
        }
    }

    // ENCRIPT
    private fun encrypt(dataToEncrypt: String, key: String, cypherSymmetricKey: Key): String {

        // EX ->
        // DESCRIPTOGRAFADO | CRIPTOGRAFADO em UFT-8 | ENVOLVIDO em Base64
        // meu_conteúdo     | 1A2b3456a7851dth4523w  | a24dgg3215dgdf4554sgd454eerrsa645

        // PROCESSO DE CRIPTOGRAFIA --->
        return getCipherByMode(key, cypherSymmetricKey, Cipher.ENCRYPT_MODE)
            .doFinal(dataToEncrypt.toByteArray(Charsets.UTF_8))
            .let { Base64.encodeToString(it, BASE_64_MODE) }
            .also { preferences.edit { putString(key, it) } }
    }

    // DECRIPT
    private fun decrypt(key: String, cypherSymmetricKey: Key): String? {
        // FAZ O INVERSO DO METODO encrypt()
        return preferences.getString(key, null)
            ?.let { Base64.decode(it, BASE_64_MODE) }
            ?.let { getCipherByMode(key, cypherSymmetricKey, Cipher.DECRYPT_MODE).doFinal(it) }
            ?.toString(Charsets.UTF_8)
    }

}

const val ALIAS_BIOMETRIC_AUTHENTICATION = "alias_biometric_authentication"
interface SecureBiometricStorageType : StorageType {
    fun getInitializedBiometricCipher(
        alias: String = ALIAS_BIOMETRIC_AUTHENTICATION,
        mode: Int
    ): Cipher

    fun putStringEncryptedUsingBiometricCipher(
        alias: String,
        dataToEncrypt: String,
        authenticatedEncryptionCipher: Cipher
    ): String

    fun getStringDecryptedUsingBiometricCipher(
        alias: String,
        authenticatedDecryptionCipher: Cipher
    ): String?
}
class LocalBiometricStorage(private val preferences: SharedPreferences) :
    LocalSecureStorage(preferences), SecureBiometricStorageType {

    override fun getInitializedBiometricCipher(alias: String, mode: Int): Cipher {
        val secretKey = getOrGenerateKey(alias, true)
        return getCipherByMode(alias, secretKey, mode)
    }

    override fun putStringEncryptedUsingBiometricCipher(
        alias: String,
        dataToEncrypt: String,
        authenticatedEncryptionCipher: Cipher
    ): String {
        return authenticatedEncryptionCipher
            .doFinal(dataToEncrypt.toByteArray(Charsets.UTF_8))
            .let { Base64.encodeToString(it, Base64.NO_WRAP) }
            .also { preferences.edit { putString(alias, it) } }
    }

    override fun getStringDecryptedUsingBiometricCipher(
        alias: String,
        authenticatedDecryptionCipher: Cipher
    ): String? {
        return preferences.getString(alias, null)
            ?.let { Base64.decode(it, Base64.NO_WRAP) }
            ?.let { authenticatedDecryptionCipher.doFinal(it) }
            ?.toString(Charsets.UTF_8)
    }
}