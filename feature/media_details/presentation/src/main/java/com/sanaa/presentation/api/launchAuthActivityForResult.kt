package com.sanaa.presentation.api

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import com.sanaa.api.AuthenticationApi


@Composable
fun launchAuthActivityForResult(loggedInWithSessionId:()-> Unit,loggedInAsGuest:()-> Unit) :ManagedActivityResultLauncher<Intent, ActivityResult>{
    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {result ->
        when(result.resultCode){
            AuthenticationApi.RESULT_LOGGED_WITH_SESSION_ID -> {
                loggedInWithSessionId()
            }
            AuthenticationApi.RESULT_LOGGED_AS_GUEST -> {
                loggedInAsGuest
            }
        }
    }
}