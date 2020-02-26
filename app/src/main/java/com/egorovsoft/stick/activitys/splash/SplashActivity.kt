package com.egorovsoft.stick.activitys.splash

import android.os.Handler
import com.egorovsoft.stick.base.BaseActivity
import org.koin.android.viewmodel.ext.android.viewModel
import com.egorovsoft.stick.activitys.MainActivity as MainActivity

class SplashActivity : BaseActivity<Boolean?>() {                                                                                                                                                                                                                                                                                                            //Я копипастил код с урока и не заметил эту надпись

    override val model: SplashViewModel by viewModel()

    override val layoutRes: Int? = null

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({ model.requestUser() }, 1000)
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