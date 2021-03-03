package com.pidh.a5plus.helper

import android.app.AlertDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

object Util {
    fun AlertDialog(context: Context?, msg: String) {
        AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert)
            .setMessage(msg)
            .setPositiveButton("Ok", null)
            .create()
            .show()
    }

    fun Date.dateToString(format: String = "yyyy-MM-dd HH:mm:ss"): String =
        SimpleDateFormat(format, Locale.getDefault()).format(this)

    fun Date.add(field: Int, amount: Int): Date {
        Calendar.getInstance().apply {
            time = this@add
            add(field, amount)
            return time
        }
    }

    fun Date.addMonths(months: Int): Date =
        add(Calendar.MONTH, months)

    fun Date.addDays(days: Int): Date =
        add(Calendar.DAY_OF_MONTH, days)
}