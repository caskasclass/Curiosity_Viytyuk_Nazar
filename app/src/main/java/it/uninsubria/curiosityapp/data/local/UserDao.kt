package it.uninsubria.curiosityapp.data.local

import androidx.room.*
import it.uninsubria.curiosityapp.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("UPDATE users SET sliderPreference = :value WHERE email = :email")
    suspend fun updateSliderPreference(email: String, value: Float)

}