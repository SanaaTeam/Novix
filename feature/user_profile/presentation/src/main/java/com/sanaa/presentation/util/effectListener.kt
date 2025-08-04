package com.sanaa.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope

@Composable
fun <E> E.Listen(onEffect: suspend CoroutineScope.(currentEffect: E) -> Unit) {
    LaunchedEffect(key1 = this) {
        onEffect(this@Listen)
    }
}
