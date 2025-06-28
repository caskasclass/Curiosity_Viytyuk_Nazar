package it.uninsubria.curiosityapp.ui.activities

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.ui.fragments.LoginFragment
import it.uninsubria.curiosityapp.ui.fragments.SignUpFragment

class AccediRegistratiActivity : AppCompatActivity() {
    private lateinit var loginButton: TextView
    private lateinit var sighUpButton: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.accedi_registrati)
        // Richiesta permesso per notifiche (solo Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permission = android.Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(permission), 1001)
            } else {
                // Debug (
                Toast.makeText(this, "Permesso notifiche già concesso", Toast.LENGTH_SHORT).show()
            }
        }

        loginButton = findViewById(R.id.LoginLabel)
        sighUpButton = findViewById(R.id.SignUpLabel)
        loadFragment(LoginFragment())

        loginButton.setOnClickListener{
            updateButtons(true)
            loadFragment(LoginFragment())
        }
        sighUpButton.setOnClickListener{
            updateButtons(false)
            loadFragment(SignUpFragment())
        }

    }

    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.login_signup_layout, fragment)
            .commit()
    }

    private fun updateButtons(isLoginActive: Boolean) {
        loginButton.isEnabled = !isLoginActive
        loginButton.setTextColor(
            ContextCompat.getColor(
                this,
                if (isLoginActive) R.color.text_disable else R.color.text_color
            )
        )
        sighUpButton.isEnabled = isLoginActive
        sighUpButton.setTextColor(
            ContextCompat.getColor(
                this,
                if (isLoginActive) R.color.text_color else R.color.text_disable
            )
        )
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permesso concesso
            } else {
                Toast.makeText(this, "Permesso notifiche necessario per usare l'app", Toast.LENGTH_LONG).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    finishAffinity()
                }, 2000)
            }
        }
    }

}