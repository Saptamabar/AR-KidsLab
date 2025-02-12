package com.example.ar_kidslab.View

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Cameraview(modifier: Modifier){
        Box(modifier = modifier.fillMaxSize()){
            Text(text = "Anjayy")
            Button(modifier = modifier.align(Alignment.BottomCenter), onClick = {}) { }
    }
}