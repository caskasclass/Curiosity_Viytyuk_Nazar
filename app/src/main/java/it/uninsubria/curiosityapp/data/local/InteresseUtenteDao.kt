package it.uninsubria.curiosityapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.uninsubria.curiosityapp.data.model.InteresseUtente

@Dao
interface InteresseUtenteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addInteresseUtente(interesseUtente: InteresseUtente)

    @Query("SELECT interesseNome FROM interesseutente WHERE userEmail = :email")
    suspend fun getInteressiByUser(email: String): List<String>

    @Delete
    suspend fun removeInteresseUtente(interesseUtente: InteresseUtente)
}