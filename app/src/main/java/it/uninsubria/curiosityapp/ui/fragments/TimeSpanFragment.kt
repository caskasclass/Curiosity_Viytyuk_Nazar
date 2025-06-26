package it.uninsubria.curiosityapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.slider.Slider
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.utils.showToast
import kotlinx.coroutines.launch

class TimeSpanFragment :Fragment() {
    private lateinit var session : SessionManager
    private lateinit var slider: Slider
    private lateinit var saveButton: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_timespan, container, false)
        session = SessionManager(requireContext())
        slider = view.findViewById(R.id.hourSlider)
        saveButton = view.findViewById(R.id.saveButton)
        slider.value=session.getSliderValue()

        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        slider.addOnChangeListener{ slider,value,fromUser ->
            Log.d("SliderValue", "Valore selezionato: $value")
            if (fromUser) {
                session.saveSliderValue(value)
            }
        }

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                val email = session.getUserEmail()

                if (email != null) {
                    val db = DatabaseProvider.getDatabase(requireContext())
                    db.userDao().updateSliderPreference(session.getUserEmail().toString(),session.getSliderValue())

                    context?.showToast("Tutto è stato salvato corretamente.")

                }
            }

        }
    }
}