package it.uninsubria.curiosityapp.ui.fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import it.uninsubria.curiosityapp.R
import it.uninsubria.curiosityapp.data.local.DatabaseProvider
import kotlinx.coroutines.launch
import android.util.Log
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.shape.ShapeAppearanceModel
import it.uninsubria.curiosityapp.data.model.InteresseUtente
import it.uninsubria.curiosityapp.data.session.SessionManager

class InterestsCollectionFragment :Fragment() {
    private lateinit var chipGroup: ChipGroup
    private lateinit var saveButton: Button
    private lateinit var session : SessionManager

    private val selectedInterests = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        session = SessionManager(requireContext())
        return  inflater.inflate(R.layout.fragment_interestscollection, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chipGroup = view.findViewById(R.id.interestsGroup)
        saveButton = view.findViewById(R.id.saveButton)


        lifecycleScope.launch {
            val db = DatabaseProvider.getDatabase(requireContext())
            val allInterests = db.interesseDao().getAllInteressi()
            val email = session.getUserEmail()

            val userInterests = if(email!=null){
                db.interesseUtenteDao().getInteressiByUser(email)
            }else emptyList()

            Log.d("TAG","Numero di interests : "+allInterests.size.toString())

            for (interesse in allInterests) {
                val chip = Chip(requireContext()).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    text = interesse.nome
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    val colorStateList = ContextCompat.getColorStateList(requireContext(), R.color.colorstatelist)
                    chipBackgroundColor =colorStateList
                    chipStrokeWidth = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        1f,
                        resources.displayMetrics
                    )
                    chipStrokeColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white_outline))
                    val radius = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        20f,
                        resources.displayMetrics
                    )
                    val shapeModel = ShapeAppearanceModel.Builder()
                        .setAllCornerSizes(radius)
                        .build()
                    shapeAppearanceModel = shapeModel
                    chipStartPadding = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        resources.displayMetrics
                    )
                    chipEndPadding = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        10f,
                        resources.displayMetrics
                    )
                    minHeight = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        60f,
                        resources.displayMetrics
                    ).toInt()
                    isCheckedIconVisible = false
                    isCheckable = true
                    isChecked = userInterests.contains(interesse.nome)
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        if (isChecked) {
                            selectedInterests.add(buttonView.text.toString())
                        } else {
                            selectedInterests.remove(buttonView.text.toString())
                        }
                        saveButton.isEnabled = selectedInterests.size >= 3
                    }
                }
                chipGroup.addView(chip)
                if (userInterests.contains(interesse.nome)) {
                    selectedInterests.add(interesse.nome)
                }
            }
            saveButton.isEnabled = selectedInterests.size >= 3
        }



        saveButton.setOnClickListener{
            lifecycleScope.launch {
                val db = DatabaseProvider.getDatabase(requireContext())
                val email = session.getUserEmail() ?: return@launch // se null esco


                db.interesseUtenteDao().deleteInteressiByUser(email)

                val nuoviInteressi = selectedInterests.map { interesseNome ->
                    InteresseUtente(userEmail = email, interesseNome = interesseNome)
                }

                db.interesseUtenteDao().addInteressiUtente(nuoviInteressi)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.interests_layout,TimeSpanFragment())
                    .addToBackStack(null)
                    .commit()

            }
        }
    }

}