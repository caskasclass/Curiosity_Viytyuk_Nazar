package it.uninsubria.curiosityapp.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.ui.activities.HomePageActivity
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var emailVar: TextView
    private lateinit var loginButtonVar: Button
    private lateinit var infLabel :TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_login, container, false)

        emailVar = view.findViewById(R.id.email_field)
        loginButtonVar = view.findViewById(R.id.loginButton)
        infLabel = view.findViewById(R.id.infLabel)



        loginButtonVar.setOnClickListener{
            val emailInput = emailVar.text.toString().trim()


            if (emailInput.isEmpty()) {
                infLabel.text = "Inserisci la email"
                return@setOnClickListener
            }


            if (!isEmailValid(emailInput)) {
                infLabel.text = "Email non valida"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val db = DatabaseProvider.getDatabase(requireContext())
                val user = db.userDao().getUserByEmail(emailInput)
                if (user == null) {
                    infLabel.text = "Utente non registrato"
                } else {
                    infLabel.text = ""
                    openHomeActivity()
                }
            }

        }

        return view

    }
    // Funzione di validazione email
    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }
    // Funzione per aprire nuova Activity
    private fun openHomeActivity() {
        val intent = Intent(requireContext(), HomePageActivity::class.java)
        startActivity(intent)
        requireActivity().finish()  // Se vuoi chiudere il fragment/activity attuale
    }
}
