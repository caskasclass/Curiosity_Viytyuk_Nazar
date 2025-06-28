package it.uninsubria.curiosityapp.worker

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.utils.StatsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val email = SessionManager(context).getUserEmail()  // Ottieni email da sessione

        if (email != null) {
            CoroutineScope(Dispatchers.IO).launch {
                when (action) {
                    "ACTION_KNEW" -> {
                        Log.d("NotificationAction", "Utente sapeva la curiosità")
                        StatsManager.updateStat(context, email, knew = true)
                    }
                    "ACTION_DIDNT_KNOW" -> {
                        Log.d("NotificationAction", "Utente NON sapeva la curiosità")
                        StatsManager.updateStat(context, email, knew = false)
                    }
                }
            }
        } else {
            Log.w("NotificationAction", "Email utente non trovata in sessione, statistica non aggiornata")
        }

        // Cancella la notifica attiva
        val notificationId = 1001
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}