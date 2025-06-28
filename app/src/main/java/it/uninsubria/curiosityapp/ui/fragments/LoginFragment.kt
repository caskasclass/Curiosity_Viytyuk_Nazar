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
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.ui.activities.InitializationPageActivity
import it.uninsubria.curiosityapp.utils.isEmailValid
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var emailVar: TextView
    private lateinit var loginButtonVar: Button
    private lateinit var infLabel :TextView
    private lateinit var session: SessionManager
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
                    session = SessionManager(requireContext())
                    session.saveSliderValue(user.sliderPreference ?: 3f)
                    session.saveUserEmail(user.email)
                    session.setLogin(true)
                    openInitializationActivity()
                }
            }

        }

        return view

    }


    private fun openInitializationActivity() {
        val intent = Intent(requireContext(), InitializationPageActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
