package com.exuberant.salveo.ui.predict

import android.animation.LayoutTransition
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.exuberant.salveo.R
import com.exuberant.salveo.data.entities.BiologicalData
import com.exuberant.salveo.data.network.response.PredictionResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_result.*
import kotlinx.android.synthetic.main.bottom_sheet_result.view.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_result_verification.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class ResultBottomSheet : BottomSheetDialogFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: PredictionViewModelFactory by instance()
    private lateinit var viewModel: PredictViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_result, container, false)
        val transition = LayoutTransition()
        transition.setAnimateParentHierarchy(false)
        view.bs_bottom_sheet_result.layoutTransition = transition
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize()
    }

    private fun initialize() {
        val sharedPreferences: SharedPreferences = MainActivity.sharedPreference
        val age = sharedPreferences.getString("age", "55")!!
        val gender = sharedPreferences.getString("gender", "1")!!
        val chestPain = sharedPreferences.getString("chestPain", "1")!!
        val bloodPressure = sharedPreferences.getString("bloodPressure", "130")!!
        val cholesterolLevel = sharedPreferences.getString("cholesterolLevel", "245")!!
        val maxHeartRate = sharedPreferences.getString("maxHeartRate", "150")!!
        verifyDetails(age, gender, chestPain, bloodPressure, cholesterolLevel, maxHeartRate)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PredictViewModel::class.java)
    }

    private fun verifyDetails(
        age: String,
        gender: String,
        chestPain: String,
        bloodPressure: String,
        cholesterolLevel: String,
        maxHeartRate: String
    ) {
        tv_gender_verification.text = "gender: ${if (gender == "0") "female" else "male"}"
        tv_age_verification.text = "age: $age"
        tv_chest_pain_verification.text = "chest pain: ${when (chestPain) {
            "0" -> getString(R.string.typical_anginal_text)
            "1" -> getString(R.string.atypical_anginal_text)
            "2" -> getString(R.string.non_anginal_text)
            else -> getString(R.string.asymptomatic_text)
        }} pain"
        tv_heart_rate_verification.text = "maximum heart rate: $maxHeartRate bpm"
        tv_blood_pressure_verification.text = "resting blood pressure: $bloodPressure mm-Hg"
        tv_cholesterol_level_verification.text = "cholesterol level: $cholesterolLevel mg/dl"
        verification_container.visibility = View.VISIBLE
        btn_confirm_details.setOnClickListener {
            verification_container.visibility = View.GONE
            tv_prediction.visibility = View.GONE
            la_loading_animation.visibility = View.VISIBLE
            val biologicalData =
                BiologicalData(
                    age,
                    bloodPressure,
                    chestPain,
                    cholesterolLevel,
                    gender,
                    maxHeartRate
                )
            testService(
                biologicalData
            )
        }
        btn_take_back.setOnClickListener {
            verification_container.visibility = View.GONE
            this@ResultBottomSheet.dismiss()
            MainActivity.bottomSheetControl.expandBottomSheet()
        }
    }

    private fun testService(biologicalData: BiologicalData) {
        viewModel.prediction.observe(viewLifecycleOwner, Observer{
            processPrediction(it)
        })
        la_loading_animation.visibility = View.VISIBLE
        bs_bottom_sheet_result.backgroundTintList =
            context!!.resources.getColorStateList(R.color.colorPrimaryDark)
        tv_prediction.visibility = View.GONE
        GlobalScope.launch(Dispatchers.Main) {
             viewModel.getPrediction(biologicalData)
        }
    }

    private fun processPrediction(predictionResponse: PredictionResponse?) {
        viewModel.prediction.removeObservers(this)
        la_loading_animation.visibility = View.GONE
        when (predictionResponse?.prediction) {
            0 -> {
                tv_prediction.text = "you have no risk of heart disease"
                tv_prediction.visibility = View.VISIBLE
                bs_bottom_sheet_result.backgroundTintList =
                    context!!.resources.getColorStateList(R.color.negativeResult)
            }
            else -> {
                tv_prediction.text = "you have risk of heart disease"
                tv_prediction.visibility = View.VISIBLE
                bs_bottom_sheet_result.backgroundTintList =
                    context!!.resources.getColorStateList(R.color.positiveResult)
            }
        }
    }
}