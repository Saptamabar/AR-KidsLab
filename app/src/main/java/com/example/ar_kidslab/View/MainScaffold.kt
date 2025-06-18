package com.example.ar_kidslab.View

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ar_kidslab.Navigation.Routes
import com.example.ar_kidslab.R
import com.example.ar_kidslab.View.ARList.ARListScreen
import com.example.ar_kidslab.View.Camera.CameraViewScreen
import com.example.ar_kidslab.View.Components.BottomNavigationBar
import com.example.ar_kidslab.View.Profile.ProfileScreen


@Composable
fun MainScaffold() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isOnCameraScreen = currentRoute == Routes.CameraView
    var isBottomBarVisible = !isOnCameraScreen
    var isFloatingButtonVisible = !isOnCameraScreen

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {

                BottomNavigationBar(navController)
            }
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = isFloatingButtonVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically()
            ) {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(Routes.CameraView)
                        isBottomBarVisible = false
                        isFloatingButtonVisible = false
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.offset(y = 50.dp).size(80.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.camera_svgrepo_com),
                        contentDescription = "Camera",
                        tint = MaterialTheme.colorScheme.onTertiary,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            val topPadding = innerPadding.calculateTopPadding()
            var bottomPadding = innerPadding.calculateBottomPadding()
            if (bottomPadding > 0.dp) bottomPadding -= 15.dp
            NavHost(navController, startDestination = Routes.Dashboard) {
                composable(Routes.Dashboard) {
                    ProfileScreen(Modifier.padding(top = topPadding, bottom = bottomPadding))
                }
                composable(Routes.ARList) {
                    ARListScreen(Modifier.padding(top = topPadding, bottom = bottomPadding))
                }
                composable(Routes.CameraView) {
                    CameraViewScreen(onBack = {
                        navController.popBackStack()
                        Modifier.padding(innerPadding)
                    })
                }
            }
        }
    )
}