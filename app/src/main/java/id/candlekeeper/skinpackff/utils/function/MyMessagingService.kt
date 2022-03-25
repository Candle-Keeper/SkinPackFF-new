package id.candlekeeper.skinpackff.utils.function

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import id.candlekeeper.skinpackff.R
import id.candlekeeper.skinpackff.ui.home.HomeActivity

class MyMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {

            if (it.imageUrl.toString().isNotEmpty()) {
                Glide.with(applicationContext)
                    .asBitmap()
                    .load(it.imageUrl.toString())
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {}
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            buildNotification(it.title, it.body, true, resource)
                        }
                    })
            } else {
                buildNotification(it.title, it.body, false, null)
            }
        }
    }

    private fun buildNotification(
        title: String?,
        content: String?,
        withImage: Boolean,
        contentBitmap: Bitmap?
    ) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getActivity(applicationContext, 0, intent, 0)
        }

        val notificationBuilder = buildNotification(applicationContext, CHANNEL_ID) {
            setSmallIcon(R.drawable.ic_notifications)
            setContentTitle(title)
            setContentText(content)
            setAutoCancel(true)
            setSound(defaultSoundUri)
            setStyle(NotificationCompat.BigTextStyle().bigText(content))
            setContentIntent(pendingIntent)
            priority = NotificationCompat.PRIORITY_DEFAULT

            if (withImage) {
                setLargeIcon(contentBitmap)
            }
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun buildNotification(
        context: Context,
        channelId: String,
        block: NotificationCompat.Builder.() -> Unit = {}
    ) =
        NotificationCompat.Builder(context, channelId).apply(block)

    companion object {
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "channel_0"
        const val CHANNEL_NAME = "channel_name"
    }
}