package com.gouveia.studiesmain.utils.extensions

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.Uri
import android.os.*
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.SHOW_FORCED
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.gouveia.studiesmain.BuildConfig
import com.gouveia.studiesmain.dca.dialogs.AlertDialogFullscreen
import com.gouveia.studiesmain.ClearableCoroutineScope
import com.gouveia.studiesmain.utils.providers.CoroutineContextProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.crypto.Cipher

/** OFUSCAMENTO DE TELA NO FRAGMENT - BY ME */
fun Fragment.setObfuscation(overshadow: Boolean) {
    if (overshadow) {
        this.activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
    } else {
        this.activity?.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        )
    }
}

/** NAVEGA PARA O DESTINO INDICADO ATRAVES DO ID DO NAGIVATION GRAPH */
fun Fragment.navTo(@IdRes dest: Int) = findNavController().navigate(dest)
fun Fragment.navTo(directions: NavDirections) = findNavController().navigate(directions)
fun Fragment.navTo(@IdRes dest: Int, args: Bundle) = findNavController().navigate(dest, args)
fun Fragment.navBack() = findNavController().navigateUp()

/** EXIBE UMA MENSAGEM SIMPLES TEMPORARIZADA NA TELA DO CELULAR */
fun Fragment.showToast(
    msg: String,
    duration: Int = Toast.LENGTH_SHORT
) = Toast.makeText(requireContext(), msg, duration).show()

/** EXIBE UMA MENSAGEM SIMPLES TEMPORARIZADA POREM RETANGULAR. TAMB??M PODE DAR A OPC??O DE ADICIONAR AC??ES */
fun Fragment.showSnake(view: View, msg: String) =
    Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()

/** DISPARA UMA ACTIVITY PARTINDO DE UM FRAGMENT */
fun Fragment.startActivity(clazz: Class<*>, name: String = "", args: Bundle = Bundle()) {
    val intent = Intent(requireContext(), clazz).apply {
        if (!(name.isNotEmpty() && args.isEmpty)) {
            putExtra(name, args)
        }
    }
    requireActivity().startActivity(intent)
}

/** NAVEGAR PARA EXIBI????O DE VIDEO NO YOUTUBE */
fun Fragment.showYoutubeVideo(videoId: String) {
    val openURL = Intent(Intent.ACTION_VIEW)
    openURL.data = Uri.parse("https://youtu.be/$videoId")
    startActivity(openURL)
}

/** VERIFICA SE A PERMISS??O FOI CONCEDIDA: https://youtu.be/grYUKZDTzVA */
fun Fragment.hasPermission(permission: String): Boolean {
    val permissionCheckResult = ContextCompat.checkSelfPermission(requireContext(), permission)
    return permissionCheckResult == PackageManager.PERMISSION_GRANTED
}

/** VERIFICA SE DEVE SOLICITAR AS PERMISS??ES NOVAMENTE: https://youtu.be/grYUKZDTzVA */
fun Fragment.shouldRequestPermission(permissions: Array<String>): Boolean {
    val grantedPermissions = mutableListOf<Boolean>()
    permissions.forEach { permission ->
        grantedPermissions.add(hasPermission(permission))
    }
    return grantedPermissions.any { granted -> !granted }
}

/** ESCONDE O TECLADO: https://youtu.be/OzK1fJi9FiQ  */
fun Fragment.hideKeyboard(view: View? = activity?.window?.decorView?.rootView) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view?.hideKeyboard(view)
    } else {
        inputMethodManager()?.hideSoftInputFromWindow(view?.applicationWindowToken, 0)
    }
}

/** EXIBE O TECLADO: https://youtu.be/OzK1fJi9FiQ  */
fun Fragment.showKeyboard(view: View? = activity?.currentFocus) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        view?.showKeyboard(view)
    } else {
        view?.let {
            it.postDelayed({
                it.requestFocus()
                inputMethodManager()?.showSoftInput(it, SHOW_FORCED)
            }, 100)
        }
    }
}

/** FAZ COM QUE O APARELHO VIBRE PELO TEMPO DEFINIDO: https://youtu.be/ogxgiaCq_24  */
@Suppress("DEPRECATION")
fun Fragment.vibrate(duration: Long = 100) {
    val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val vm =
                requireContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.S -> {
            vibrator?.vibrate(
                VibrationEffect.createOneShot(
                    duration,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        }
        else -> vibrator?.vibrate(duration)
    }
}

/** VERIFICA SE TEM REDE E SE TEM ACESSO A INTERNET: https://youtu.be/DpyxLwibE0M  */
fun Fragment.hasInternet(): Boolean {
    val connMgr =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = connMgr.getNetworkCapabilities(connMgr.activeNetwork)
        capabilities != null &&
                // verifica se voc?? tem rede ex: WIFI, etc.
                capabilities.hasCapability(NET_CAPABILITY_INTERNET) &&
                // e realmetne consegue fazer requisi????es, pois em alguns casos
                // ex. aeroporto vc esta conectado, porem ainda n??o foi liberado
                // e por isso n??o tem rede
                capabilities.hasCapability(NET_CAPABILITY_VALIDATED)
    } else {
        @Suppress("DEPRECATION")
        connMgr.activeNetworkInfo?.isConnected == true
    }
}

/** EXIBE LEITOR DE BIOMETRIA: XXXXXXX */
fun Fragment.launchBiometricAuth(
    title: String,
    message: String? = null, //Opcional, caso queira exibir uma mensagem...
    negativeLabel: String, // Descri????o do bot??o Cancelar...
    confirmationRequired: Boolean = true,
    initializedCipher: Cipher? = null, // Opcional, caso vc mesmo(Seu App) queira manter o controle sobre os acessos
    onAuthenticationSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
    onAuthenticationError: (Int, String) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(requireContext())
    val prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            Timber.d("Autentica????o Biometrica - Sucesso.")
            onAuthenticationSuccess(result)
        }

        override fun onAuthenticationError(errorCode: Int, errorMessage: CharSequence) {
            Timber.d("Autentica????o Biometrica - Erro.")
            onAuthenticationError(errorCode, errorMessage.toString())
        }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .apply { if (message != null) setDescription(message) }
        .setConfirmationRequired(confirmationRequired)
        .setNegativeButtonText(negativeLabel)
        .build()

    initializedCipher?.let {
        val cryptoObject = BiometricPrompt.CryptoObject(initializedCipher)
        prompt.authenticate(promptInfo, cryptoObject)
        return
    }

    prompt.authenticate(promptInfo)
}

/** NAVEGAR PARA PLAYSTORE */
fun Fragment.openPlayStore() {
    Intent(Intent.ACTION_VIEW).apply {
        data =
            Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        setPackage("com.android.vending")
    }.let { startActivity(it) }
}

/** EXIBIR MATERIAL ALERT DIALOG DE ACORDO COM SUAS NECESSIDADES */
fun Fragment.showDefaultMaterialAlertDialog(
    title: String? = null,
    message: String? = null,
    positiveButtonLabel: String? = null,
    positiveButtonClickListener: () -> Unit = {},
    negativeButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    cancelable: Boolean = false,
    cancelListener: () -> Unit = {},
    dismissListener: () -> Unit = {},
) {
    MaterialAlertDialogBuilder(requireContext())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonLabel) { dialog, _ -> dialog.dismiss(); positiveButtonClickListener() }
        .setNegativeButton(negativeButtonLabel) { dialog, _ -> dialog.dismiss(); negativeButtonClickListener() }
        .setCancelable(cancelable)
        .setOnCancelListener { cancelListener() }
        .setOnDismissListener { dismissListener() }
        .create().also { it.show() }
}

/** EXIBIR ALERTA SEMI-CUSTOMIZADOS (APENAS COM CONTEUDO EST??TICO CUSTOMIZADO) */
fun Fragment.showDefaultMaterialAlertDialogWithCustomStaticContent(
    positiveButtonLabel: String? = null,
    positiveButtonClickListener: () -> Unit = {},
    negativeButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    cancelable: Boolean = false,
    cancelListener: () -> Unit = {},
    dismissListener: () -> Unit = {},
    @LayoutRes customLayoutId: Int,
    @StyleRes styleId: Int? = null,
    @DrawableRes customBackgroundId: Int? = null
) {
    // Customiza tema do Dialog.
    val builder = if (styleId != null) MaterialAlertDialogBuilder(
        ContextThemeWrapper(
            requireContext(),
            styleId
        )
    ) else MaterialAlertDialogBuilder(requireContext())

    // Customiza conte??do do Dialog.
    builder.setView(customLayoutId)

    val dialog = builder
        .setPositiveButton(positiveButtonLabel) { dialog, _ -> dialog.dismiss(); positiveButtonClickListener() }
        .setNegativeButton(negativeButtonLabel) { dialog, _ -> dialog.dismiss(); negativeButtonClickListener() }
        .setCancelable(cancelable)
        .setOnCancelListener { cancelListener() }
        .setOnDismissListener { dismissListener() }
        .create()

    // Customiza o background do Dialog.
    if (customBackgroundId != null) {
        dialog.window?.setBackgroundDrawableResource(customBackgroundId)
    }
    dialog.show()
}

/** CRIAR ALERTAS TOTALMENTE CUSTOMIZADOS */
fun Fragment.createFullCustomAlertDialog(
    customView: View,
    @StyleRes styleId: Int? = null,
    @DrawableRes customBackgroundId: Int? = null
): AlertDialog {
    // Customiza tema do Dialog.
    val builder = if (styleId != null) MaterialAlertDialogBuilder(
        ContextThemeWrapper(
            requireContext(),
            styleId
        )
    ) else MaterialAlertDialogBuilder(requireContext())

    builder.setView(customView)
    val dialog = builder.create()

    // Customiza o background do Dialog.
    if (customBackgroundId != null) {
        dialog.window?.setBackgroundDrawableResource(customBackgroundId)
    }
    return dialog
}

/** DIALOG FULLSCREEN PERSONALIZADO */
fun Fragment.showFullscreenAlertDialog(
    title: String,
    message: String,
    positiveButtonLabel: String = getString(android.R.string.ok),
    positiveButtonClickListener: () -> Unit = {},
    cancelButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    dismissAction: () -> Unit = {},
) = AlertDialogFullscreen(
    title = title,
    message = message,
    positiveLabel = positiveButtonLabel,
    positiveAction = positiveButtonClickListener,
    negativeLabel = cancelButtonLabel,
    negativeAction = negativeButtonClickListener,
    dismissAction = dismissAction,
).also { it.show(parentFragmentManager, it.javaClass.simpleName) }

/** REALIZAR UMA TAREFA EM LOOP (EX: CONSULTAR ALGUM RESULTADO DE UM SERVIDOR) */
fun Fragment.polling(
    isOffline: () -> Boolean = { false },
    onOffline: () -> Unit = {},
    isCompleted: () -> Boolean = { false },
    onCompleted: () -> Unit = {},
    isError: () -> Boolean = { false },
    onError: () -> Unit = {},
    isCanceled: () -> Boolean = { false },
    onCanceled: () -> Unit = {},
    pollingDelayInMilliSeconds: Long = 5000L
) {
    val pollingScope = ClearableCoroutineScope(CoroutineContextProvider().ui)
    pollingScope.launch {
        if (isOffline()) {
            handleStateChange(onOffline, pollingScope)
            return@launch
        }
        when {
            isCompleted() -> {
                handleStateChange(onCompleted, pollingScope)
                return@launch
            }
            isError() -> {
                handleStateChange(onError, pollingScope)
                return@launch
            }
            isCanceled() -> {
                handleStateChange(onCanceled, pollingScope)
                return@launch
            }
            else -> {
                showToast("Polling a cada 5 segundos!")
                delay(pollingDelayInMilliSeconds)
                pollingScope.clearScope()
                polling(
                    isOffline = isOffline,
                    onOffline = onOffline,
                    isCompleted = isCompleted,
                    onCompleted = onCompleted,
                    isError = isError,
                    onError = onError,
                    isCanceled = isCanceled,
                    onCanceled = onCanceled
                )
            }
        }
    }
}

private fun handleStateChange(handle: () -> Unit, pollingScope: ClearableCoroutineScope) {
    handle()
    pollingScope.clearScope()
    return
}

fun Fragment.openPhoneDial(phoneNumber: String) {
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("tel:$phoneNumber")
    }.let { startActivity(it) }
}

fun Fragment.setDefaultTheme(@StyleRes styleId: Int) {
    requireActivity().setTheme(styleId)
}

fun Fragment.showTransparentStatusBar(isTransparent: Boolean) {
    val translucentColor = 0x04000000
    val window = requireActivity().window
    if (isTransparent) window.addFlags(translucentColor) else window.clearFlags(translucentColor)
}

//fun Fragment.setSystemStatusBarColorOverColorResource(@ColorRes id: Int) {
//    requireActivity().window.statusBarColor = requireActivity().getColor(id)
//}
//
//fun Fragment.setSystemNavigationBarColorOverColorResource(@ColorRes id: Int) {
//    requireActivity().window.navigationBarColor = requireActivity().getColor(id)
//}
//
//fun Fragment.setSystemStatusBarColorOverAttrResource(@AttrRes id: Int) {
//    requireActivity().window.statusBarColor = getColor(id)
//}
//
//fun Fragment.setSystemNavigationBarColorOverAttrResource(@AttrRes id: Int) {
//    requireActivity().window.navigationBarColor = getColor(id)
//}

//fun Fragment.getColor(@AttrRes id: Int): Int {
//    val typedValue = TypedValue()
//    requireContext().theme.resolveAttribute(id, typedValue, true)
//    return typedValue.data
//}

fun Fragment.setTranslucentWindow(translucent: Boolean) {
    if (translucent) {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    } else {
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
    }
}

fun Fragment.hideTopActionBar() = activity?.let { (it as AppCompatActivity).hideActionBar() }
fun Fragment.showTopActionBar() = activity?.let { (it as AppCompatActivity).showActionBar() }
fun Fragment.setActionBarTitle(@StringRes title: Int) =
    activity?.let { (it as AppCompatActivity).setActionBarTitle(title) }

fun Fragment.setActionBarIcon(@DrawableRes icon: Int) =
    activity?.let { (it as AppCompatActivity).setActionBarIcon(icon) }

//fun Fragment.setToolBarIcon(@DrawableRes icon:Int) =  activity?.let { (it as AppCompatActivity).setToolbarNavigationIcon(icon) }
fun Fragment.setToolBarTitle(title: String) =
    activity?.let { (it as AppCompatActivity).setToolbarNavigationTitle(title) }

fun openUrl(activity: Activity, url: String) {
    activity.startActivity(Intent(ACTION_VIEW, Uri.parse(url)))
}

//fun Fragment.openAppSettings() {
//    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
//        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
//    }
//    if (canHandleIntent(requireContext(), intent)) {
//        requireContext().startActivity(intent)
//    } else {
//        requireContext().startActivity(Intent(Settings.ACTION_SETTINGS))
//    }
//}

// ==================================================
// - Query Packages Android 12    -------------------
// ==================================================
/** PRECISA ESPECIFICAR A PERMISSION NO MANIFEST A PARTIR DO ANDROID 12 */
fun canHandleIntent(context: Context, intent: Intent): Boolean {
    return context.packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        .isNotEmpty()
}

// VEJA VIDEO: NAVEGA????O MULTIPLA: https://youtu.be/CwwU1D91elg
fun <T : AppCompatActivity> Fragment.navigateToNavGraph(
    entryPoint: Class<T>,
    @IdRes navGraphStartDestination: Int? = null,
    overridePendingTransition: Boolean = false
) {
    requireActivity().navigateToNavGraph(
        entryPoint,
        navGraphStartDestination,
        overridePendingTransition
    )
}


fun Fragment.startFurtaCorAnim(view: TextView, fromColor: Int, toColor: Int) {
    val furtaCorAnim: ValueAnimator = ObjectAnimator.ofInt(
        view,
        "textColor",
        fromColor,
        toColor
    )
    furtaCorAnim.duration = 1000
    furtaCorAnim.setEvaluator(ArgbEvaluator())
    furtaCorAnim.repeatCount = ValueAnimator.INFINITE
    furtaCorAnim.repeatMode = ValueAnimator.REVERSE
    furtaCorAnim.start()
}

fun Fragment.getDrawable(@DrawableRes id: Int): Drawable? {
    return ContextCompat.getDrawable(requireContext(), id)
}

//fun Fragment.getColor(@ColorRes id: Int): Int {
//    return requireActivity().resources.getColor(id, null)
//}

fun Fragment.showDialog(
    title: String? = null,
    message: String,
    positiveButtonLabel: String,
    positiveButtonClickListener: () -> Unit = {},
    negativeButtonLabel: String? = null,
    negativeButtonClickListener: () -> Unit = {},
    cancelable: Boolean = true,
    cancelListener: () -> Unit = {}
) = MaterialAlertDialogBuilder(requireContext())
    .setTitle(title)
    .setMessage(message)
    .setPositiveButton(positiveButtonLabel) { _, _ -> positiveButtonClickListener() }
    .setNegativeButton(negativeButtonLabel) { _, _ -> negativeButtonClickListener() }
    .setCancelable(cancelable)
    .setOnCancelListener { cancelListener() }
    .create()
    .also { it.show() }

// GETS
fun Fragment.inputMethodManager() =
    context?.getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
