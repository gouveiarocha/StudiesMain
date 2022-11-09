package com.gouveia.studiesmain.gdc.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.gouveia.studiesmain.R
import com.gouveia.studiesmain.databinding.FragmentGdcNotificationBinding

private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETED_NOTIFICATIONS"

class GdcNotificationFragment : Fragment(R.layout.fragment_gdc_notification) {

    private lateinit var binding: FragmentGdcNotificationBinding
    private lateinit var notificationManager: NotificationManager

    private val notificationReceiver = NotificationReceiver()
    private val dynamicReceiver = DynamicReceiver()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGdcNotificationBinding.bind(view)

        initBtnListeners()
        setupBtnStates(enableNotify = true, enableUpdate = false, enableCancel = false)
        createNotificationChannel()
        registerNotificationReceiver()

        registerDynamicReceiver(dynamicReceiver)

    }

    private fun initBtnListeners() {
        binding.notify.setOnClickListener { sendNotification() }    //enviar notificação padrão
        binding.update.setOnClickListener { updateNotification() }  //customizar\personalizar
        binding.cancel.setOnClickListener { cancelNotification() }  //remover da barra de status
    }

    // ASSEGURAR ESTADO INICIAL DOS BOTÕES
    private fun setupBtnStates(
        enableNotify: Boolean,
        enableUpdate: Boolean,
        enableCancel: Boolean
    ) {
        binding.notify.isEnabled = enableNotify
        binding.update.isEnabled = enableUpdate
        binding.cancel.isEnabled = enableCancel
    }

    // (>=API26) - DEFINIR CANAL DE NOTIFICAÇÃO
    // A partir do android 8.0 (api 26) temos que definir o canal para que o usuário possa
    // eventualmente desabilitar as notificações do aplicativo através das configurações
    private fun createNotificationChannel() {
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Isso aqui é que aparece nas configurações do aparelho la no seu aplicativo
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Mascot Notification", NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableVibration(true)
                description = "Notification from Mascot"
                // Serão exibidas se o telefone der suporte a essas coisas
                enableLights(true)
                lightColor = Color.RED
            }
            // Criar o canal com as propriedades definidas
            notificationManager.createNotificationChannel(notificationChannel)
        } else {
            // (<API26)
            // Sua tarefinha de casa :) caso vc tenha um aparelho inferior a api 26
        }
    }

    private fun sendNotification() {

        val notificationBuilder = getNotificationBuilder()

        // Cria ações da notificação.
        createNotificationAction(notificationBuilder, NOTIFICATION_ID, ACTION_UPDATE, "Atualize")
        createNotificationAction(notificationBuilder, NOTIFICATION_ID, ACTION_CANCEL, "Remover")

        // Config remove com slide left/right ou lixeirinha
        val deleteIntent = Intent(ACTION_DELETE_ALL)
        val deletedAction = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            deleteIntent,
            PendingIntent.FLAG_ONE_SHOT
        )
        notificationBuilder.setDeleteIntent(deletedAction)

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        // Como neste passo aqui a notificação foi enviada, eu deshabilito o botão de enviar
        // e habilito os botões de customização e cancelamento
        setupBtnStates(enableNotify = false, enableUpdate = true, enableCancel = true)

    }

    // PERSONALIZA A NOTIFICAÇÃO COM UM ICONE
    private fun updateNotification() {
        // Converte o icone em um bitmap.
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.ic_notification)
        // Atualizando o estilo e o titulo
        val notification = getNotificationBuilder()
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(androidImage)
                    .setBigContentTitle("Notificação atualizada!")
            )
        // Atualizar a notificação atual
        notificationManager.notify(NOTIFICATION_ID, notification.build())
        // E habilitar o botão de cancelamento
        setupBtnStates(enableNotify = false, enableUpdate = false, enableCancel = true)
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setupBtnStates(enableNotify = true, enableUpdate = false, enableCancel = false)
    }

    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter = Intent(actionId) //para broadcast receiver
        val updateAction = PendingIntent.getBroadcast(
            requireContext(), notificationId, updateActionFilter, PendingIntent.FLAG_ONE_SHOT
        )
        builder.addAction(
            // Mudanças nas notificação desde o Android N, esse icone nao aparece mais e esta
            // presente apenas para manter compatibilidade em aparelhos antigos. Em compensação
            // se ganhou mais espaço para os titulos.
            // https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
            R.drawable.ic_launcher_foreground,
            actionTitle,
            updateAction
        )
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), GdcNotificationFragment::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID).apply {
            setContentTitle("Você recebeu uma notificação!")
            setContentText("Valeu, já vou me inscrever no canal!")
            setSmallIcon(R.drawable.ic_notification_update)
            setContentIntent(notificationPendingIntent)
            priority = NotificationCompat.PRIORITY_HIGH
            setDefaults(NotificationCompat.DEFAULT_ALL)
            setAutoCancel(false)
        }

    }

    private fun registerNotificationReceiver() {
        val notificationActionFilters = IntentFilter()
        notificationActionFilters.addAction(ACTION_UPDATE)
        notificationActionFilters.addAction(ACTION_CANCEL)
        notificationActionFilters.addAction(ACTION_DELETE_ALL)
        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)
    }


    inner class NotificationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Atualiza a notificação
            when (intent.action) {
                ACTION_UPDATE -> updateNotification()
                ACTION_CANCEL -> {
                    notificationManager.cancel(NOTIFICATION_ID)
                    setupBtnStates(enableNotify = true, enableUpdate = false, enableCancel = false)
                }
                ACTION_DELETE_ALL -> setupBtnStates(
                    enableNotify = true,
                    enableUpdate = false,
                    enableCancel = false
                )
            }
        }
    }

    // (>=API26) A maioria dos BroadcastReceivers são declarados dinamicamente.
    private fun registerDynamicReceiver(dynamicReceiver: BroadcastReceiver) {
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            requireActivity().registerReceiver(dynamicReceiver, it)
        }
    }

    private fun unregisterNotificationReceiver() {
        requireActivity().unregisterReceiver(notificationReceiver)
    }

    private fun unregisterDynamicReceiver() {
        requireActivity().unregisterReceiver(dynamicReceiver)
    }

    /** EVITAR VAZAMENTO DE MEMÓRIA NO BROADCASTRECEIVER */

    // Como boa prática, devemos desregistrar nosso Receiver - Abaixo fazemos isso como exemplo
    // em dois ciclos de vidas, mas isso vai depender da nossa necessidade.

    // EX NO ONSTOP
    override fun onStop() {
        super.onStop()
        // Após parar, desregistramos.
        unregisterDynamicReceiver()
    }

    //EX NO ONDESTROY
    override fun onDestroy() {
        // Antes de destruir, desregistramos.
        unregisterNotificationReceiver()
        super.onDestroy()
    }

}