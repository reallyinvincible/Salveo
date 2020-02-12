package com.exuberant.salveo.ui.predict

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.util.TypedValue
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.ImageViewCompat
import com.exuberant.salveo.MainBottomSheetControlInterface
import com.exuberant.salveo.R
import com.exuberant.salveo.ui.splash.SplashActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_main.*
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity(),
    MainBottomSheetControlInterface {

    private lateinit var updateTipRunnable: Runnable
    private lateinit var updateTipHandler: Handler
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private var genderSwitch = mutableListOf(false, false)
    private var heartPainCategory = mutableListOf(false, false, false, false)

    companion object {
        lateinit var sharedPreference: SharedPreferences
        lateinit var bottomSheetControl: MainBottomSheetControlInterface
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showSplash()
        setContentView(R.layout.activity_main)
        initialize()
    }

    private fun showSplash() {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
    }

    private fun initialize() {
        //Setting up SharedPreferences
        sharedPreference = getSharedPreferences("Data", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()

        //Setting up TextSwitchers
        ts_tips.setFactory {
            val textView = TextView(this@MainActivity)
            textView.textSize = 24f
            textView.setTextColor(resources.getColor(R.color.colorText))
            return@setFactory textView
        }
        ts_tip_number.setFactory {
            val numberTextView = TextView(this@MainActivity)
            numberTextView.textSize = 28f
            numberTextView.setTextColor(resources.getColor(R.color.colorText))
            numberTextView.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
            return@setFactory numberTextView
        }

        //Setting up Tips
        val tipList = listOf(resources.getStringArray(R.array.heart_health_tips))[0]
        val random = Random()
        updateTipHandler = Handler()
        updateTipRunnable = Runnable {
            run {
                val num: Int = random.nextInt(tipList.size)
                ts_tips.setText(tipList[num].toString())
                ts_tip_number.setText("Tip #" + (num + 1).toString())
                updateTipHandler.postDelayed(updateTipRunnable, 7000)
            }
        }
        updateTipHandler.postDelayed(updateTipRunnable, 0)

        //Setting up BottomSheet
        bottomSheetBehavior = from(bs_bottom_sheet_main)
        bottomSheetBehavior.peekHeight = getDips(70f)
        Handler().postDelayed({
            bottomSheetBehavior.state = STATE_HALF_EXPANDED
            Handler().postDelayed({
                bottomSheetBehavior.state = STATE_COLLAPSED
            }, 1500)
        }, 3000)
        bottomSheetControl = this@MainActivity

        setListeners()
    }

    private fun setListeners(){
        iv_man.setOnClickListener {
            selectMan()
        }
        iv_woman.setOnClickListener {
            selectWoman()
        }

        rb_typical_angina.setOnClickListener { setPainCategory(0) }
        rb_atypical_angina.setOnClickListener { setPainCategory(1) }
        rb_non_anginal.setOnClickListener { setPainCategory(2) }
        rb_asymptomatic.setOnClickListener { setPainCategory(3) }
        btn_start_analysis.setOnClickListener { getData() }
    }

    private fun getData(){
        val gender = if(genderSwitch[0]) "0" else "1"
        val chestPain = when {
            heartPainCategory[0] -> {
                "1"
            }
            heartPainCategory[1] -> {
                "2"
            }
            heartPainCategory[2] -> {
                "3"
            }
            else -> {
                "4"
            }
        }
        val age = ms_age_slider.value.toInt().toString()
        val bloodPressure = ms_blood_pressure_slider.value.toInt().toString()
        val cholesterolLevel = ms_cholesterol_slider.value.toInt().toString()
        val maxHeartRate = ms_heart_rate_slider.value.toInt().toString()
        bottomSheetBehavior.state = STATE_COLLAPSED
        saveData(age, gender, chestPain, bloodPressure, cholesterolLevel, maxHeartRate)
    }

    private fun saveData(
        age: String,
        gender: String,
        chestPain: String,
        bloodPressure: String,
        cholesterolLevel: String,
        maxHeartRate: String
    ) {
        editor.putString("age", age)
        editor.putString("gender", gender)
        editor.putString("chestPain", chestPain)
        editor.putString("bloodPressure", bloodPressure)
        editor.putString("cholesterolLevel", cholesterolLevel)
        editor.putString("maxHeartRate", maxHeartRate)
        editor.apply()
        ResultBottomSheet()
            .show(supportFragmentManager, "")
    }

    private fun selectMan() {
        genderSwitch[0] = false
        genderSwitch[1] = true
        processGenderTints()
    }

    private fun selectWoman() {
        genderSwitch[0] = true
        genderSwitch[1] = false
        processGenderTints()
    }

    private fun clearGenderTints() {
        ImageViewCompat.setImageTintList(
            iv_man,
            ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
        )
        ImageViewCompat.setImageTintList(
            iv_woman,
            ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
        )
    }

    private fun processGenderTints() {
        clearGenderTints()
        if (genderSwitch[0]) {
            ImageViewCompat.setImageTintList(iv_woman, null)
            ImageViewCompat.setImageTintList(
                iv_man,
                ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
            )
        } else {
            ImageViewCompat.setImageTintList(iv_man, null)
            ImageViewCompat.setImageTintList(
                iv_woman,
                ColorStateList.valueOf(resources.getColor(R.color.lightGrey))
            )
        }
    }

    private fun clearAllRadios(){
        rb_typical_angina.isChecked = false
        rb_atypical_angina.isChecked = false
        rb_non_anginal.isChecked = false
        rb_asymptomatic.isChecked = false
    }

    private fun setPainCategory(index: Int){
        clearAllRadios()
        heartPainCategory[index] = true
        when (index) {
            0 -> rb_typical_angina.isChecked = true
            1 -> rb_atypical_angina.isChecked = true
            2 -> rb_non_anginal.isChecked = true
            else -> rb_asymptomatic.isChecked = true
        }
    }

    private fun getDips(pixels: Float) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, pixels, resources.displayMetrics
    ).roundToInt()

    override fun collapseBottomSheet() {
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

    override fun expandBottomSheet() {
        bottomSheetBehavior.state = STATE_EXPANDED
    }

}
