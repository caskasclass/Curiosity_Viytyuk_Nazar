package it.uninsubria.curiosityapp.utils

import android.content.Context
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.model.StatsTuple

object StatsManager {
    suspend fun updateStat(context: Context, email: String, knew: Boolean) {
        val db = DatabaseProvider.getDatabase(context)
        if (knew) {
            db.userDao().incrementKnewCount(email)
        } else {
            db.userDao().incrementDidntKnowCount(email)
        }
    }

    suspend fun getStats(context: Context, email: String): StatsTuple {
        val db = DatabaseProvider.getDatabase(context)
        return db.userDao().getStats(email)
    }
}