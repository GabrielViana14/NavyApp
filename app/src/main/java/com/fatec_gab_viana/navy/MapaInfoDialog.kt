package com.fatec_gab_viana.navy

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class MapaInfoDialog(private val info: String): DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Detalhe do Marcador")
            .setMessage(info)
            .setPositiveButton("Ok"){_,_->}



        return builder.create()
    }


}