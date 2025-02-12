package com.example.ar_kidslab

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigator
import kotlinx.serialization.Serializable

@Serializable
object dashboard
@Serializable
object arList
@Serializable
object cameraview


// Model untuk item navigasi
data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val route: Any
)

