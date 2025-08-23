package com.sanaa.tvapp.logging

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class CrashReportingTree(
    private val crashlytics: FirebaseCrashlytics,
) : Timber.Tree() {

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
    ) {
        val level = priority.toReadableName()
        val safeTag = tag ?: "NoTag"
        crashlytics.log("[$level][$safeTag] $message")

        if (priority >= Log.WARN && t != null) {
            crashlytics.recordException(t)
        }

        Log.println(priority, safeTag, message)
        t?.let { Log.println(priority, safeTag, Log.getStackTraceString(it)) }
    }

    private fun Int.toReadableName(): String = when (this) {
        Log.VERBOSE -> "VERBOSE"
        Log.DEBUG -> "DEBUG"
        Log.INFO -> "INFO"
        Log.WARN -> "WARN"
        Log.ERROR -> "ERROR"
        else -> toString()
    }
}