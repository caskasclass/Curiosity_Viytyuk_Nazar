package it.uninsubria.curiosityapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.local.InteresseDao
import it.uninsubria.curiosityapp.data.model.Interesse
import it.uninsubria.curiosityapp.ui.activities.AccediRegistratiActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(this@MainActivity)


            withContext(Dispatchers.IO) {
                inserisciInteressiSeVuoti(db.interesseDao())
            }

            startActivity(Intent(this@MainActivity, AccediRegistratiActivity::class.java))
            finish()
        }
    }
    suspend fun inserisciInteressiSeVuoti(interesseDao: InteresseDao) {
        val count = interesseDao.getCount()
        if (count == 0) {
            val interessi = listOf(
                Interesse("Scienza"),
                Interesse("Tecnologia"),
                Interesse("Storia"),
                Interesse("Spazio"),
                Interesse("Animali"),
                Interesse("Corpo Umano"),
                Interesse("Cinema"),
                Interesse("Musica"),
                Interesse("Turismo")
            )
            interessi.forEach {
                Log.d("TAG", "inserisco : $it")
                interesseDao.insertInteresse(it)
            }
        }
    }
}


