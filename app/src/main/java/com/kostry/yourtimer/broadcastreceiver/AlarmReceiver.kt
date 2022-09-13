package com.kostry.yourtimer.broadcastreceiver

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import com.kostry.yourtimer.R
import com.kostry.yourtimer.ui.mainactivity.MainActivity
import com.kostry.yourtimer.ui.timer.TimerFragment
import com.kostry.yourtimer.util.TIMER_NOTIFICATION_ID

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            NotificationManagerCompat
                .from(context)
                .notify(TIMER_NOTIFICATION_ID,
                    createNotification(context.getString(R.string.time_is_out), context))
        }
    }

    private fun createNotification(message: String, context: Context): Notification {
        val pendingIntent: PendingIntent = NavDeepLinkBuilder(context)
            .setGraph(R.navigation.main_nav_graph)
            .setDestination(R.id.timerFragment)
            .setArguments(args = bundleOf(TimerFragment.TIMER_FRAGMENT_ARGS_KEY to 0))
            .createPendingIntent()

        return NotificationCompat.Builder(context, MainActivity.NOTIFICATION_CHANNEL_ID)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setAutoCancel(true)
            .build()
    }


    companion object {

        fun newIntent(context: Context): Intent {
            return Intent(context, AlarmReceiver::class.java)
        }
    }
}