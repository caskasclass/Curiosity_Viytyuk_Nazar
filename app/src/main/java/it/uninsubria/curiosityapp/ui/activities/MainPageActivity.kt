package it.uninsubria.curiosityapp.ui.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.work.WorkManager
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.local.AppDatabase
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.ui.fragments.DidYouKnowThatFragment
import it.uninsubria.curiosityapp.utils.loadFragment


class MainPageActivity: AppCompatActivity() {
    private lateinit var mainContentContainer: ConstraintLayout
    private lateinit var  userGreetingsLabel:TextView
    private lateinit var settingsButton:ImageButton
    private lateinit var logoutButton:ImageButton
    private lateinit var session:SessionManager
    private lateinit var db: AppDatabase
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_page)
        db = DatabaseProvider.getDatabase(this)
        session = SessionManager(this)
        mainContentContainer = findViewById(R.id.main_content_container)
        userGreetingsLabel = findViewById(R.id.user_label)
        settingsButton = findViewById(R.id.settings_button)
        logoutButton = findViewById(R.id.logout_button)
        val username = intent.getStringExtra("username")
        userGreetingsLabel.text = "Ciao, $username"


        val curiosityFactJson = intent.getStringExtra("curiosity_fact_json")

        val fragment = DidYouKnowThatFragment().apply {
            arguments = Bundle().apply {
                putString("curiosity_fact_json", curiosityFactJson)
            }
        }

        loadFragment(mainContentContainer.id,fragment)


        logoutButton.setOnClickListener{
            logout(this)
        }


    }
    private fun logout(context: Context) {

        session.clearSession()
        WorkManager.getInstance(context).cancelUniqueWork("CuriosityNotificationWork")

        val intent = Intent(context, AccediRegistratiActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        context.startActivity(intent)
    }
}