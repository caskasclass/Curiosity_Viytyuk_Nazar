package it.uninsubria.curiosityapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.gson.Gson
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.model.CuriosityFact
import it.uninsubria.curiosityapp.data.network.fetchCuriosityFact
import it.uninsubria.curiosityapp.data.session.SessionManager
import it.uninsubria.curiosityapp.utils.StatsManager
import it.uninsubria.curiosityapp.utils.createCuriosityFact
import kotlinx.coroutines.launch
class DidYouKnowThatFragment :Fragment() {
    private lateinit var iknow_button:Button
    private lateinit var ididntknow_button:Button
    private  lateinit var curiosityText: TextView
    private  lateinit var curiosityCategory: TextView
    private  lateinit var curiosityImage: ImageView
    private lateinit var session:SessionManager
    private  lateinit var know_label: TextView
    private  lateinit var total_label: TextView
    private  lateinit var didntknow_label: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_did_you_know_that, container, false)
        session = SessionManager(requireContext())
        iknow_button = view.findViewById(R.id.iknow)
        ididntknow_button = view.findViewById(R.id.didntknow)
        curiosityText = view.findViewById(R.id.curiosityText)
        curiosityCategory = view.findViewById(R.id.curiosityCategory)
        curiosityImage = view.findViewById(R.id.curiosityImage)
        know_label = view.findViewById(R.id.yes)
        total_label = view.findViewById(R.id.tot)
        didntknow_label = view.findViewById(R.id.no)
        session.getUserEmail()?.let { updateStatsUI(it) }


        val curiosityFactJson = arguments?.getString("curiosity_fact_json")
        if (!curiosityFactJson.isNullOrEmpty()) {
            val curiosityFact = Gson().fromJson(curiosityFactJson, CuriosityFact::class.java)
            buildCuriosityUi(curiosityFact)
            Log.d("DEBUG","Argomenti ricevuti")
        } else {
            Log.d("DEBUG","Argomenti non ricevuti dalla notifica")
            nextCuriosity()
        }

        iknow_button.setOnClickListener {
            lifecycleScope.launch {
                session.getUserEmail()?.let { email ->
                    StatsManager.updateStat(requireContext(), email, knew = true)
                    updateStatsUI(email)
                }
                nextCuriosity()
            }

        }
        ididntknow_button.setOnClickListener {
            lifecycleScope.launch {
                session.getUserEmail()?.let { email ->
                    StatsManager.updateStat(requireContext(), email, knew = false)
                    updateStatsUI(email)
                }
                nextCuriosity()
            }
        }

        return view
    }
    private fun updateStatsUI(email: String) {
        lifecycleScope.launch {
            val stats = StatsManager.getStats(requireContext(), email)
            val total = stats.knewCount + stats.didntKnowCount
            if (total > 0) {
                val knewPercent = stats.knewCount * 100.0 / total
                val didntKnowPercent = stats.didntKnowCount * 100.0 / total
                val knewPercentStr = formatPercentage(knewPercent)
                val didntKnowPercentStr = formatPercentage(didntKnowPercent)
                total_label.text = "Tot: $total"
                know_label.text = knewPercentStr
                didntknow_label.text =didntKnowPercentStr
            } else {

                know_label.text = "0%"
                didntknow_label.text = "0%"
            }
        }
    }
    private fun formatPercentage(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toInt().toString() + "%"
        } else {
            String.format("%.1f%%", value)
        }
    }
    private fun nextCuriosity(){
        lifecycleScope.launch {
            val curiosity = fetchCuriosityFact()
            curiosity?.let {
                Log.d("CURIOSITY",it.text)
                buildCuriosityUi(createCuriosityFact(it.text))
            }
        }
    }


    private fun buildCuriosityUi(curiosityFact: CuriosityFact){
        Glide.with(this)
            .load(curiosityFact.immagineUrl)
            .into(curiosityImage)
        curiosityCategory.text = curiosityFact.categoria
        curiosityText.text = curiosityFact.testo

    }


}

