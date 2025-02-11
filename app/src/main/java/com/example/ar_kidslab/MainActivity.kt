package com.example.ar_kidslab

import android.os.Bundle
import com.example.ar_kidslab.ui.theme.ARKidsLabTheme
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ARKidsLabTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavBarWithFAB(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavBarWithFAB(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val navItemList = listOf(
        NavigationItem("Avatar", ImageVector.vectorResource(R.drawable.vector), arList),
        NavigationItem("Profile", ImageVector.vectorResource(R.drawable.dribbble_light_preview), dashboard)
    )

    var selectedIndex by remember { mutableStateOf(1) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.primary,modifier = Modifier.clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))) {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentDestination = navBackStackEntry?.destination
                navItemList.forEachIndexed {index, navigationItem ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(selectedIconColor = MaterialTheme.colorScheme.secondary, indicatorColor = MaterialTheme.colorScheme.secondary),
                        selected =  currentDestination?.hierarchy?.any {
                            it.hasRoute(navigationItem.route::class)
                        } == true,
                        onClick = {
                            selectedIndex = index
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Handle FAB click */ },
                containerColor = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .offset(y = 50.dp)
                    .size(80.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.camera_svgrepo_com),
                    contentDescription = "Camera",
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)  // Ukuran ikon di dalam FAB
                )
            }
        },
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
                    Avatar(modifier.padding(top = topPadding,
                        bottom = bottomPadding))
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

@Composable
fun Profile(modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Blue)) {
        Text(text = "Profile")
    }
}

@Composable
fun Avatar(modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.Green)) {
        Text(text = "Avatar")
    }
}
