package it.uninsubria.curiosityapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import it.uninsubria.curiosityapp.data.model.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
