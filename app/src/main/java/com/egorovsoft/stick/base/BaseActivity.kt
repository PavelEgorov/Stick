package com.egorovsoft.stick.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.egorovsoft.stick.data.errors.NoAuthException
import com.firebase.ui.auth.AuthUI

abstract class BaseActivity<T, S : BaseViewState<T>> : AppCompatActivity() {
    abstract val viewModel: BaseViewModel<T, S>
    abstract val layoutRes: Int?

    companion object {
        const val RC_SIGN_IN = 4242
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layoutRes?.let {  setContentView(it) }
        viewModel.getViewState().observe(this, object : Observer<S> {
            override fun onChanged(t: S?) {
                t ?: return
                t.error?.let {
                    renderError(it)
                    return
                }
                renderData(t.data)
            }
        })
    }

    abstract fun renderData(data: T)

    private fun renderError(error: Throwable) {
        when (error) {
            is NoAuthException -> startLogin()
            else -> error.message?.let { showError(it) }
        }
    }

    private fun startLogin() {
        val providers = listOf(
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RC_SIGN_IN && resultCode != Activity.RESULT_OK){
            finish()
        }
    }

    protected fun showError(error: String){
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }
}