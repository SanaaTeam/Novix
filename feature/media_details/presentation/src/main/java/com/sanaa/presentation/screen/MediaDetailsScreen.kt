package com.sanaa.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.sanaa.designsystem.design_system.component.novix_scaffold.NovixScaffold
import com.sanaa.designsystem.design_system.theme.NovixTheme

@Composable
fun MediaDetailsScreen(mediaId: Int, onBackClick: () -> Unit) {
    NovixTheme(isDarkMode = true) {
        NovixScaffold {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Media Details for ID: $mediaId", fontSize = 24.sp)
                Button(onClick = onBackClick) {
                    Text("Go Back")
                }
            }
        }
    }
}