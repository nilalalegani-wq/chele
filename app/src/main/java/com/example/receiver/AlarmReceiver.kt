package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.data.SettingsManager

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val settings = SettingsManager(context)
        val action = intent.action ?: return

        if (action == ACTION_BOOT_COMPLETED) {
            // Re-schedule all active alarms on boot
            NotificationHelper.scheduleAllAlarms(context, settings)
            return
        }

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannels(context, notificationManager)

        when (action) {
            ACTION_DAILY_REMINDER -> {
                if (settings.getDailyNotificationEnabled()) {
                    showNotification(
                        context,
                        notificationManager,
                        CHANNEL_DAILY_ID,
                        NOTIFICATION_DAILY_ID,
                        "یادآوری چله زیارت عاشورا",
                        "امروز اعمال چله خود را انجام داده‌اید؟ التماس دعا."
                    )
                }
            }
            ACTION_SADAGAH_REMINDER -> {
                if (settings.getCharityNotificationEnabled()) {
                    showNotification(
                        context,
                        notificationManager,
                        CHANNEL_SADAGAH_ID,
                        NOTIFICATION_SADAGAH_ID,
                        "یادآوری پرداخت صدقه",
                        "امروز صدقه روزانه خود را فراموش نکنید. التماس دعا."
                    )
                }
            }
        }
    }

    private fun showNotification(
        context: Context,
        notificationManager: NotificationManager,
        channelId: String,
        notificationId: Int,
        title: String,
        text: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Fallback standard icon, can use app icon
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }

    private fun createNotificationChannels(context: Context, manager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val dailyChannel = NotificationChannel(
                CHANNEL_DAILY_ID,
                "یادآوری روزانه اعمال چله",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "یادآوری برای قرائت زیارت عاشورا و اعمال روزانه چله"
            }

            val sadagahChannel = NotificationChannel(
                CHANNEL_SADAGAH_ID,
                "یادآوری صدقه روزانه",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "یادآوری روزانه ساعت ۱۰ صبح برای پرداخت صدقه"
            }

            manager.createNotificationChannel(dailyChannel)
            manager.createNotificationChannel(sadagahChannel)
        }
    }

    companion object {
        const val ACTION_DAILY_REMINDER = "com.example.cheleh.ACTION_DAILY_REMINDER"
        const val ACTION_SADAGAH_REMINDER = "com.example.cheleh.ACTION_SADAGAH_REMINDER"
        const val ACTION_BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED"

        const val CHANNEL_DAILY_ID = "cheleh_daily_reminder_channel"
        const val CHANNEL_SADAGAH_ID = "cheleh_sadagah_reminder_channel"

        const val NOTIFICATION_DAILY_ID = 1001
        const val NOTIFICATION_SADAGAH_ID = 1002
    }
}
