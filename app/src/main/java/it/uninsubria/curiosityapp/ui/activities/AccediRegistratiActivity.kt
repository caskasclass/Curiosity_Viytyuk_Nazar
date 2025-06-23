package it.uninsubria.curiosityapp.ui.activities

import android.os.Bundle
import android.widget.TextView
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

        loginButton = findViewById<TextView>(R.id.LoginLabel)
        sighUpButton = findViewById<TextView>(R.id.SignUpLabel)
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
            .replace(R.id.login_signup_fragment, fragment)
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

}