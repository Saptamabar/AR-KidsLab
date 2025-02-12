package com.example.ar_kidslab

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ar_kidslab.View.ARList
import com.example.ar_kidslab.View.Cameraview
import com.example.ar_kidslab.View.Profile
import com.example.ar_kidslab.ui.theme.ARKidsLabTheme

class MainActivity : ComponentActivity() {
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setCameraPreview()
            } else {
                showPermissionDeniedUI()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) -> {
                setCameraPreview()
            }
            else -> {
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }


    private fun setCameraPreview() {
        setContent {
            ARKidsLabTheme {
                NavBarWithFAB()
            }
        }
    }
    private fun showPermissionDeniedUI() {
        setContent {
            ARKidsLabTheme {
                ShowPermissionDeniedScreen()
            }
        }
    }

    @Composable
    fun ShowPermissionDeniedScreen() {
        val context = LocalContext.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Camera permission is required to use this feature.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Redirect to app settings
                (context as? MainActivity)?.openAppSettings()
            }) {
                Text("Open Settings")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Offer an alternative
                println("Alternative action")
            }) {
                Text("Use Alternative")
            }
        }
    }


}

@Composable
fun NavBarWithFAB(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navItemList = listOf(
        NavigationItem("AR List", ImageVector.vectorResource(R.drawable.vector), arList),
        NavigationItem("Profile", ImageVector.vectorResource(R.drawable.dribbble_light_preview), dashboard)
    )
    var isFloatingButtonVisible by remember { mutableStateOf(true) }
    var isBottomBarVisible by remember { mutableStateOf(true) }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = isBottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ){
            if (isBottomBarVisible){
            NavigationBar(containerColor = MaterialTheme.colorScheme.primary,modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )) {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentDestination = navBackStackEntry?.destination
                navItemList.forEach {navigationItem ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.secondary, indicatorColor = MaterialTheme.colorScheme.secondary),
                        selected =  currentDestination?.hierarchy?.any {
                            it.hasRoute(navigationItem.route::class)
                        } == true,
                        onClick = {
                            navController.navigate(navigationItem.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(tint = Color.White,
                                imageVector = navigationItem.icon,
                                contentDescription = navigationItem.label,
                                modifier = Modifier.size(width = 24.dp, height = 24.dp)
                            )
                        },
                        label = {
                            Text(color = Color.White,text = navigationItem.label)
                        },
                    )
                }
            }
        } }},
        floatingActionButton = {
            AnimatedVisibility(visible = isFloatingButtonVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically()) {  if(isFloatingButtonVisible){
            FloatingActionButton(
                onClick = { navController.navigate(cameraview)
                            isBottomBarVisible = false
                            isFloatingButtonVisible = false
                          },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .offset(y = 50.dp)
                    .size(80.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.camera_svgrepo_com),
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }}},
        floatingActionButtonPosition = FabPosition.Center,
        content = { innerPadding ->
            val topPadding = innerPadding.calculateTopPadding()
            var bottomPadding = innerPadding.calculateBottomPadding()
            if (bottomPadding > 0.dp) bottomPadding -= 15.dp
            NavHost(navController, startDestination = dashboard) {
                composable<dashboard> {
                    Profile(modifier.padding(top = topPadding,
                        bottom = bottomPadding))
                }
                composable<arList> {
                    ARList(modifier.padding(top = topPadding,
                        bottom = bottomPadding))
                }
                composable<cameraview> {

                        // Tambahkan BackHandler untuk halaman cameraview
                        BackHandler(enabled = true) {
                            // Kembali ke halaman sebelumnya
                            navController.popBackStack()
                            // Tampilkan kembali BottomBar dan FAB
                            isBottomBarVisible = true
                            isFloatingButtonVisible = true
                        }


                    Cameraview()

                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ARKidsLabTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavBarWithFAB(modifier = Modifier.padding(innerPadding))
        }
    }
}


