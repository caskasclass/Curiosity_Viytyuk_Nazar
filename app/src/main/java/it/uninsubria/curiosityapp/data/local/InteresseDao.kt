package it.uninsubria.curiosityapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.uninsubria.curiosityapp.data.model.Interesse

@Dao
interface InteresseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInteresse(interesse: Interesse)

    @Query("SELECT * FROM interessi")
    suspend fun getAllInteressi(): List<Interesse>

    @Query("SELECT COUNT(*) FROM interessi")
    suspend fun getCount(): Int


}