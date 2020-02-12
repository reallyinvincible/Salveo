package com.exuberant.salveo.ui.splash

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.exuberant.salveo.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        la_splash_animation.addAnimatorListener(object: Animator.AnimatorListener{
            override fun onAnimationRepeat(p0: Animator?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onAnimationEnd(p0: Animator?) {
                this@SplashActivity.finish()
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationStart(p0: Animator?) {
            }
        })
    }

}