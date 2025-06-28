package it.uninsubria.curiosityapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.slider.Slider
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.ui.activities.MainPageActivity
import it.uninsubria.curiosityapp.utils.showToast
import it.uninsubria.curiosityapp.worker.CuriosityWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

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
                    db.userDao().updateSliderPreference(email,session.getSliderValue())


                    val timespanHours = session.getSliderValue().toLong().coerceAtLeast(3) // almeno 3 ora

                    val workRequest = PeriodicWorkRequestBuilder<CuriosityWorker>(timespanHours, TimeUnit.HOURS)
                        .build()

                    WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
                        "CuriosityNotificationWork",
                        ExistingPeriodicWorkPolicy.UPDATE,
                        workRequest
                    )

                    context?.showToast("Tutto è stato salvato corretamente.")
                    val username = db.userDao().getUserByEmail(email)?.username
                    val intent = Intent(requireContext(),MainPageActivity::class.java)
                    intent.putExtra("username",username)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

        }
    }
}