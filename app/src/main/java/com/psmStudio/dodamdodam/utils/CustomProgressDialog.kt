package com.psmStudio.dodamdodam.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.psmStudio.dodamdodam.R

class CustomProgressDialog(context: Context) : Dialog(context) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_progress)
    }
}