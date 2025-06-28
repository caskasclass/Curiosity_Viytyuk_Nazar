package it.uninsubria.curiosityapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.uninsubria.curiosityapp.data.model.InteresseUtente

@Dao
interface InteresseUtenteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addInteressiUtente(interessi: List<InteresseUtente>)

    @Query("SELECT interesseNome FROM interesseUtente WHERE userEmail = :email")
    suspend fun getInteressiByUser(email: String): List<String>

    @Query("DELETE FROM interesseUtente WHERE userEmail = :email")
    suspend fun deleteInteressiByUser(email: String)

}
