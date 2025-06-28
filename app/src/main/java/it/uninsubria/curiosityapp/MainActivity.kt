package it.uninsubria.curiosityapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import androidx.lifecycle.lifecycleScope
//import com.google.firebase.FirebaseApp
//import com.google.firebase.messaging.FirebaseMessaging
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.local.InteresseDao
import it.uninsubria.curiosityapp.data.model.Interesse
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.ui.activities.AccediRegistratiActivity
import it.uninsubria.curiosityapp.ui.activities.MainPageActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // FirebaseApp.initializeApp(this)
        /*FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("FCM_TOKEN", "Token: ${it.result}")
            }
        }*/
        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(this@MainActivity)


            withContext(Dispatchers.IO) {
                inserisciInteressiSeVuoti(db.interesseDao())
            }

            val sessionManager = SessionManager(this@MainActivity)
            if(sessionManager.isLoggedIn()){
                val user = db.userDao().getUserByEmail(sessionManager.getUserEmail()!!)
                val intent = Intent(this@MainActivity, MainPageActivity::class.java)
                intent.putExtra("username",user?.username)
                startActivity(intent)
                finish()
            }else{
                startActivity(Intent(this@MainActivity, AccediRegistratiActivity::class.java))
                finish()
            }


        }
    }

    suspend fun inserisciInteressiSeVuoti(interesseDao: InteresseDao) {
        val count = interesseDao.getCount()
        if (count == 0) {
            val interessi = listOf(
                Interesse("Animali"),
                Interesse("Persone/Famosi"),
                Interesse("TV/Media"),
                Interesse("Paesi/Geografia"),
                Interesse("Generiche"),

            )
            interessi.forEach {
                Log.d("TAG", "inserisco : $it")
                interesseDao.insertInteresse(it)
            }
        }
    }
}


