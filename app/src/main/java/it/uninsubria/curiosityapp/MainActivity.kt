package it.uninsubria.curiosityapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import it.uninsubria.curiosityapp.ui.activities.AccediRegistratiActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val intent = Intent(this, AccediRegistratiActivity::class.java)
        startActivity(intent)

        finish()
    }
}
