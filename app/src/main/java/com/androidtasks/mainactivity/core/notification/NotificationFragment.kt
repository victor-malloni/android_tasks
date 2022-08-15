package com.androidtasks.mainactivity.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.androidtasks.mainactivity.R
import com.androidtasks.mainactivity.databinding.FragmentNotificationBinding

private const val NOTIFICATION_ID = 0
private const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
private const val ACTION_UPDATE = "ACTION_UPDATE_NOTIFICATION"
private const val ACTION_CANCEL = "ACTION_CANCEL_NOTIFICATION"
private const val ACTION_DELETE_ALL = "ACTION_DELETE_ALL_NOTIFICATIONS"

class NotificationFragment : Fragment(R.layout.fragment_notification) {
    private lateinit var binding: FragmentNotificationBinding
    private lateinit var notificationManager: NotificationManager
    private val notificationReceiver = NotificationReceiver()

    override fun onViewCreated(view: View, saveInstanceState: Bundle?) {
        super.onViewCreated(view, saveInstanceState)
        setupUiButtonListenners()
        setupUiButtonStates(enableNotify = true, enableCancel = false, enableUpdate = false)
        createNotificationChannel()
        registerNotificationReciver()

    }


    private fun setupUiButtonListenners() {
        binding.notify.setOnClickListener { sendNotification() } //notificação padrão
        binding.cancel.setOnClickListener { cancelNotification() } //customizar / personalizar
        binding.update.setOnClickListener { updateNotification() } //remover da barra de status
    }

    private fun setupUiButtonStates(
        enableNotify: Boolean,
        enableCancel: Boolean,
        enableUpdate: Boolean
    ) {
        binding.notify.isEnabled = enableNotify
        binding.cancel.isEnabled = enableCancel
        binding.update.isEnabled = enableUpdate

    }

    //Criando Notificação
    private fun createNotificationChannel() {
        notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                "Notificação",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableVibration(true)
            notificationChannel.description = "'Notification Channel description'"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.YELLOW
            //criar o canal com as propriedades definidas
            notificationManager.createNotificationChannel(notificationChannel)

        } else {
            Toast
                .makeText(requireContext(), "Versao inferior", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun updateNotification() {
        //personalizaçao dinamica da notificaçao adicionando um icone
        val iconImage = BitmapFactory.decodeResource(resources, R.drawable.ic_notification)
        //atualizando estilo e titulo
        val notification = getNotificationBuilder()
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .setBigContentTitle("notificaçao atualizada")
            )
        //atualizar notificaçao atual
        notificationManager.notify(NOTIFICATION_ID, notification.build())
        //habilitar o botao de cancelamento
        setupUiButtonStates(false, false, true)

    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(requireContext(), NotificationFragment::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            requireContext(),
            NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(requireContext(), PRIMARY_CHANNEL_ID)
            .setContentTitle("Você recebeu uma notificação!")
            .setContentText("Valeu!")
            .setSmallIcon(R.drawable.ic_notification_update)
            .setContentIntent(notificationPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(false)
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
        setupUiButtonStates(true, false, false)
    }

    private fun sendNotification() {
        val builder = getNotificationBuilder()

        createNotificationAction(builder, NOTIFICATION_ID, ACTION_UPDATE, "atualizar")
        createNotificationAction(builder, NOTIFICATION_ID, ACTION_CANCEL, "remover")

        val deleteAllAction = Intent(ACTION_DELETE_ALL)//remove com slide ou lixeira
        val deletedAction = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            deleteAllAction,
            PendingIntent.FLAG_ONE_SHOT
        )
        builder.setDeleteIntent(deletedAction)
        notificationManager.notify(NOTIFICATION_ID, builder.build())
        setupUiButtonStates(false, true, true)
    }

    private fun createNotificationAction(
        builder: NotificationCompat.Builder,
        notificationId: Int,
        actionId: String,
        actionTitle: String
    ) {
        val updateActionFilter = Intent(actionId)
        val updadeAction = PendingIntent.getBroadcast(
            requireContext(),
            notificationId,
            updateActionFilter,
            PendingIntent.FLAG_ONE_SHOT
        )
        // esse icone nao aparece mais e esta presente apenas para manter compatibilidade
        // https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
        builder.addAction(R.drawable.ic_launcher_background, actionTitle, updadeAction)
    }

    private fun registerNotificationReciver() {
        val notificationActionFilters = IntentFilter()
        notificationActionFilters.addAction(ACTION_UPDATE)
        notificationActionFilters.addAction(ACTION_DELETE_ALL)
        notificationActionFilters.addAction(ACTION_CANCEL)
        requireActivity().registerReceiver(notificationReceiver, notificationActionFilters)

    }

    inner class NotificationReceiver() : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action){
                ACTION_UPDATE -> updateNotification()
                ACTION_CANCEL -> {
                    notificationManager.cancel(NOTIFICATION_ID)
                    setupUiButtonStates(enableNotify = true, enableCancel = false, enableUpdate = false)
                }
                ACTION_UPDATE -> setupUiButtonStates(enableNotify = true, enableCancel = false, enableUpdate = false)
            }
        }

    }
}
