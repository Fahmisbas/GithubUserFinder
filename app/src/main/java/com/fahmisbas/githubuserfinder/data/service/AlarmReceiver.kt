package com.fahmisbas.githubuserfinder.data.service

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.fahmisbas.githubuserfinder.ui.searchuser.SearchUserActivity

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val notificationIntent = Intent(context, SearchUserActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0)
        AlarmHelper.showNotification(
            context,
            title ?: "title",
            message ?: "message",
            ID_REPEATING,
            pendingIntent
        )
    }

    companion object {
        const val CHANNEL_ID = "channel_reminder"
        const val CHANNEL_NAME = "daily_reminder"
        const val EXTRA_TITLE = "title_reminder"
        const val EXTRA_MESSAGE = "message_reminder"
        const val ID_REPEATING = 100
    }
}