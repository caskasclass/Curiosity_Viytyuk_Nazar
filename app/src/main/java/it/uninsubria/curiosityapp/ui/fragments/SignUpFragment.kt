package it.uninsubria.curiosityapp.ui.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import it.uninsubria.curiosityapp.R
import java.io.File
import java.io.FileOutputStream
import androidx.lifecycle.lifecycleScope
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.model.User
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {
    private lateinit var pfpImageView: ShapeableImageView
    private  lateinit var uploadButton: Button
    private lateinit var signupButton:Button
    private lateinit var errorusernameLabel : TextView
    private lateinit var  errormailLabel : TextView
    private lateinit var  informationLabel : TextView
    private var selectedProfileImageUri: Uri? = null
    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedProfileImageUri = it
            pfpImageView.setImageURI(it)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup, container, false)
        pfpImageView = view.findViewById(R.id.pfp)
        uploadButton = view.findViewById(R.id.pfp_upload)
        signupButton = view.findViewById(R.id.signup_button)
        errormailLabel= view.findViewById(R.id.mailerror)
        errorusernameLabel = view.findViewById(R.id.usernameerror)
        informationLabel = view.findViewById(R.id.infoLabel)


        uploadButton.setOnClickListener{
            pickImageLauncher.launch("image/*")
        }

        signupButton.setOnClickListener{
            val email = (view.findViewById<TextView>(R.id.email_field)).text.toString().trim()
            val username = (view.findViewById<TextView>(R.id.username)).text.toString().trim()

            if (!isEmailValid(email)) {
                errormailLabel.text = "Inserire una email valida"
                return@setOnClickListener
            }
            if (!isUsernameValid(username)) {
                errorusernameLabel.text ="Minimo 2 char, max 24 char"
                return@setOnClickListener
            }

            lifecycleScope.launch {
                registerUser(email, username, selectedProfileImageUri)

            }
        }




        return view
    }

    private fun copyImageToInternalStorage(context: Context, sourceUri: Uri, filename: String): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null
            val file = File(context.filesDir, filename)
            val outputStream = FileOutputStream(file)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            return file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }




    private suspend fun registerUser(email: String, username: String, profileUri: Uri?): Boolean {
        val db = DatabaseProvider.getDatabase(requireContext())

        val existingUser = db.userDao().getUserByEmail(email)
        if (existingUser != null) {
            informationLabel.text="Utente con questa mail è già registrato."
            return false
        }

        val profileImagePath = profileUri?.let {
            copyImageToInternalStorage(requireContext(), it, "profile_${email.hashCode()}.png")
        }

        val newUser = User(email, username, profileImagePath)
        db.userDao().insertUser(newUser)

        // QUI dovresti chiamare il backend o servizio per inviare email di benvenuto
        // Qui simuliamo con un Toast:
        Toast.makeText(requireContext(), "Grazie per esserti registrato con noi!", Toast.LENGTH_LONG).show()

        return true
    }
    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return emailRegex.matches(email)
    }

    private fun isUsernameValid(username: String): Boolean {
        val usernameRegex = Regex("^[a-zA-Z0-9_]{3,24}$")
        return usernameRegex.matches(username)
    }


}
