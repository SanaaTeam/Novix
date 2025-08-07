package com.sanaa.api

import android.content.Context
import android.content.Intent


interface OnboardingApi {
    fun getLaunchIntent(context: Context): Intent
}