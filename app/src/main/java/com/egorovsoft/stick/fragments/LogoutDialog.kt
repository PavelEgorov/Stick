package com.egorovsoft.stick.fragments

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.egorovsoft.stick.R

class LogoutDialog: DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) =
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.logout_dialog_title))
            .setMessage(getString(R.string.logout_dialog_message))
            .setPositiveButton(getString(R.string.logout_dialog_ok)) { dialog, which -> (activity as LogoutListener).onLogout() }
            .setNegativeButton(getString(R.string.logout_dialog_cancel)) { dialog, which ->  dismiss() }
            .create()


    interface LogoutListener{
        fun onLogout()
    }
}