package it.uninsubria.curiosityapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.uninsubria.curiosityapp.data.model.Interesse
import it.uninsubria.curiosityapp.data.model.InteresseUtente
import it.uninsubria.curiosityapp.data.model.User

@Database(entities = [User::class, Interesse::class, InteresseUtente::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun interesseDao(): InteresseDao
    abstract fun interesseUtenteDao(): InteresseUtenteDao
}
