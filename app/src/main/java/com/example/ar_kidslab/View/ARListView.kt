package com.example.ar_kidslab.View

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextObfuscationMode.Companion.Hidden
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ar_kidslab.ArView
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberStandardBottomSheetStateFix(
    initialValue: SheetValue = SheetValue.Hidden,
    confirmValueChange: (SheetValue) -> Boolean = { true },
    skipHiddenState: Boolean = false
): SheetState {
    val skipPartiallyExpanded = false
    return rememberSaveable(
        skipPartiallyExpanded,
        confirmValueChange,
        saver = sheetStateSaver(
            skipPartiallyExpanded = skipPartiallyExpanded,
            confirmValueChange = confirmValueChange,
            skipHiddenState = skipHiddenState
        )
    ) {
        SheetState(skipPartiallyExpanded, initialValue, confirmValueChange, skipHiddenState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
fun sheetStateSaver(
    skipPartiallyExpanded: Boolean,
    confirmValueChange: (SheetValue) -> Boolean,
    skipHiddenState: Boolean = false
) = Saver<SheetState, SheetValue>(
    save = { it.currentValue },
    restore = { savedValue ->
        SheetState(skipPartiallyExpanded, savedValue, confirmValueChange, skipHiddenState)
    }
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARList(modifier: Modifier,
           bottombarVisible : (Boolean) -> Unit,
           fabVisible : (Boolean)-> Unit,
           navController: NavController) {
    val scaffoldState = rememberBottomSheetScaffoldState( rememberStandardBottomSheetStateFix())
    val scope = rememberCoroutineScope()

    // Create the Bottom Sheet Scaffold
    BottomSheetScaffold(

        modifier = modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        sheetContent = {
            // Content of the Bottom Sheet
            Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                Text("This is a Scaffold Bottom Sheet")
                Spacer(modifier = Modifier.height(8.dp))

            }
        },
        sheetPeekHeight = 200.dp // Dynamic height
    ) {
        // Main content of the screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                scope.launch { navController.navigate(ArView) } // Expand the sheet
            }) {
                Text("Expand Bottom Sheet")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Check the current state of the bottom sheet
            when (scaffoldState.bottomSheetState.currentValue) {
                SheetValue.Hidden -> {
                    Text("Sheet is Collapsed")
                    bottombarVisible(false)
                    fabVisible(false)// Collapse the sheet
                }
                SheetValue.Expanded -> {
                    Text("Sheet is Expanded")
                    bottombarVisible(true)
                    fabVisible(true)// Collapse the sheet
                }
                SheetValue.PartiallyExpanded -> {
                    Text("Sheet is Partially Expanded")
                    bottombarVisible(true)
                    fabVisible(true)
                }
            }
        }
    }
}

@Composable
fun dDisplay() {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val cameraNode = rememberCameraNode(engine)
    cameraNode.position = Position(z = 2.5f)
    val environmentLoader = rememberEnvironmentLoader(engine)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray) // Ubah latar belakang di sini
    ) {
        Scene(
            engine = engine,
            modelLoader = modelLoader,
            cameraNode = cameraNode,
            childNodes = listOf(
                ModelNode(
                    modelInstance = modelLoader.createModelInstance("Model/dog.glb"),
                    scaleToUnits = 1.25f
                )
            ),
            environment = environmentLoader.createEnvironment(),



        )
    }
}