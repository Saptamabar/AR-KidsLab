package com.example.ar_kidslab.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun ARList(modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Green)) {
        Text(text = "Augmented Reality")
    }
}