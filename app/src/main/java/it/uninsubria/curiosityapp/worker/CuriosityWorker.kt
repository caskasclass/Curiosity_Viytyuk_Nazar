package it.uninsubria.curiosityapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.network.fetchCuriosityFact
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.ui.activities.MainPageActivity
import it.uninsubria.curiosityapp.utils.createCuriosityFact
import it.uninsubria.curiosityapp.utils.urlToBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CuriosityWorker(
    appContext: Context,
    workerParams: WorkerParameters

) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val ACTION_KNEW = "ACTION_KNEW"
        const val ACTION_DIDNT_KNOW = "ACTION_DIDNT_KNOW"
    }


    private val channelId = "curiosity_channel"
    private val notificationId = 1001

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val sessionManager = SessionManager(applicationContext)
        if (!sessionManager.isLoggedIn()) {
            // Utente non loggato, non mandiamo notifiche
            return@withContext Result.success()
        }

        createNotificationChannel()

        val curiosityFact = fetchCuriosityFact()?.let {
            createCuriosityFact(it.text)
        } ?: createCuriosityFact("Did you know? Our fact generator took a coffee break. Please try again later!")

        val imageBitmap = urlToBitmap(curiosityFact.immagineUrl)
        val gson = Gson()
        val factJson = gson.toJson(curiosityFact)

        // Intent per aprire l'app passando la curiosità
        val mainIntent = Intent(applicationContext, MainPageActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("curiosity_fact_json", factJson)
        }
        val mainPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent per azione "Lo sapevo"
        val knewIntent = Intent(applicationContext, NotificationActionReceiver::class.java).apply {
            action = ACTION_KNEW
            putExtra("curiosity_fact_json", factJson)
        }
        val knewPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            knewIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent per azione "Non lo sapevo"
        val didntKnowIntent = Intent(applicationContext, NotificationActionReceiver::class.java).apply {
            action = ACTION_DIDNT_KNOW
            putExtra("curiosity_fact_json", factJson)
        }
        val didntKnowPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            2,
            didntKnowIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Curiosità del momento")
            .setContentText(curiosityFact.testo)
            .setStyle(
                if (imageBitmap != null)
                    NotificationCompat.BigPictureStyle().bigPicture(imageBitmap)
                else
                    null
            )
            .setContentIntent(mainPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.yep, "Lo sapevo", knewPendingIntent)
            .addAction(R.drawable.nope, "Non lo sapevo", didntKnowPendingIntent)

        if (NotificationManagerCompat.from(applicationContext).areNotificationsEnabled()) {
            NotificationManagerCompat.from(applicationContext).notify(notificationId, builder.build())
        }

        Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Canale Curiosità",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifiche con curiosità personalizzate"
            }
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
