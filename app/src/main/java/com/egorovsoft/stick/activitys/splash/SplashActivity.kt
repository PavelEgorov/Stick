package com.egorovsoft.stick.activitys.splash

import android.os.Handler
import androidx.lifecycle.ViewModelProvider
import com.egorovsoft.stick.base.BaseActivity
import com.egorovsoft.stick.MainActivity as MainActivity

class SplashActivity : BaseActivity<Boolean?, SplashViewState>() {                                                                                                                                                                                                                                                                                                            //Я копипастил код с урока и не заметил эту надпись

    override val viewModel by lazy {
        ViewModelProvider(this).get(SplashViewModel::class.java)
    }

    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ viewModel.requestUser() }, 1000)
    }

    override fun renderData(data: Boolean?) {
        data?.takeIf { it }?.let {
            startMainActivity()
        }
    }

    private fun startMainActivity() {
        MainActivity.start(this)
        finish()
    }
}