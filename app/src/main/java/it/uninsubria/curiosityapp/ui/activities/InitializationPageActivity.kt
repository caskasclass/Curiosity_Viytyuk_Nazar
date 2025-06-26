package it.uninsubria.curiosityapp.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.ui.fragments.InterestsCollectionFragment
import it.uninsubria.curiosityapp.utils.loadFragment

class InitializationPageActivity : AppCompatActivity() {
    private lateinit var interests_layout:ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initialization_page)

        interests_layout = findViewById(R.id.interests_layout)

        loadFragment(R.id.interests_layout, InterestsCollectionFragment())
    }


}