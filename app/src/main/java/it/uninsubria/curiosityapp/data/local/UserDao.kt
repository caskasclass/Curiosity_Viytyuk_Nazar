package it.uninsubria.curiosityapp.data.local

import androidx.room.*
import it.uninsubria.curiosityapp.data.model.StatsTuple
import it.uninsubria.curiosityapp.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("UPDATE users SET sliderPreference = :value WHERE email = :email")
    suspend fun updateSliderPreference(email: String, value: Float)

    @Query("UPDATE users SET knewCount = knewCount + 1 WHERE email = :email")
    suspend fun incrementKnewCount(email: String)

    @Query("UPDATE users SET didntKnowCount = didntKnowCount + 1 WHERE email = :email")
    suspend fun incrementDidntKnowCount(email: String)

    @Query("SELECT knewCount, didntKnowCount FROM users WHERE email = :email")
    suspend fun getStats(email: String): StatsTuple


}