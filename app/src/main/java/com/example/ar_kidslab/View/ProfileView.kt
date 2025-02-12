package com.example.ar_kidslab.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Profile(modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Blue)) {
        Text(text = "Profile")
    }
}

@Preview
@Composable
fun ProfilePreview() {
    Profile(modifier = Modifier)
}