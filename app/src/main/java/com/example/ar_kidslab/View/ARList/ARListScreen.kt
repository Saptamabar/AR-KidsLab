package com.example.ar_kidslab.View.ARList

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.filament.Colors
import com.google.android.filament.IndirectLight
import com.google.android.filament.Skybox
import io.github.sceneview.Scene
import io.github.sceneview.collision.HitResult
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraManipulator
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironment
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARListScreen(
    modifier: Modifier = Modifier,
    setBottomBarVisible: (Boolean) -> Unit,
    setFloatingButtonVisible: (Boolean) -> Unit
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    val modelList = listOf("Ayam.glb", "Anjing.glb", "Rusa.glb")
    var selectedModel by remember { mutableStateOf(modelList.first()) }
    var modelInstance by remember { mutableStateOf<ModelInstance?>(null) }
    val centerNode = rememberNode(engine)

    // Sheet state & control
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }

    // UI toggle (FAB & bottomBar)
    var isUiVisible by remember { mutableStateOf(true) }

    LaunchedEffect(selectedModel) {
        val modelPath = "models/$selectedModel"
        modelInstance = modelLoader.createModelInstance(modelPath)
    }

    Box(modifier = modifier.fillMaxSize()) {
        // AR Scene
        Scene(
            modifier = Modifier
                .fillMaxSize(),
            engine = engine,
            view = rememberView(engine),
            renderer = rememberRenderer(engine),
            scene = rememberScene(engine).apply {
                val linearRgb = Colors.toLinear(
                    Colors.RgbaType.SRGB,
                    0.53f, 0.81f, 0.92f, 1.0f
                )
                skybox = Skybox.Builder()
                    .color(linearRgb[0], linearRgb[1], linearRgb[2], linearRgb[3])
                    .build(engine)
            },
            modelLoader = modelLoader,
            materialLoader = materialLoader,
            environmentLoader = environmentLoader,

            cameraNode = rememberCameraNode(engine) {
                position = Position(z = -4.0f, y = 1.0f, x = 1.0f)
                lookAt(centerNode)
            },
            childNodes = remember(modelInstance) {
                listOfNotNull(
                    modelInstance?.let {
                         ModelNode(
                            modelInstance = it,
                            scaleToUnits = 1.0f,
                        ).apply {

                             position = Position(x = 0.0f, y = -0.5f, z = 0.0f)
                         }

                    }
                )
            },
        )

        // Toggle UI & Modal Button
        Column(modifier = Modifier.padding(16.dp)) {
            Button(onClick = {
                isUiVisible = !isUiVisible
                setBottomBarVisible(isUiVisible)
                setFloatingButtonVisible(isUiVisible)
            }) {
                Text(if (isUiVisible) "Hide UI" else "Show UI")
            }

            Button(onClick = {
                showSheet = true
            }) {
                Text("Pilih Model 3D")
            }
        }
    }

    // Modal Bottom Sheet
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showSheet = false
            },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Pilih Model 3D",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                modelList.forEach { modelName ->
                    Button(
                        onClick = {
                            selectedModel = modelName

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(text = modelName.removeSuffix(".glb"))
                    }
                }
            }
        }
    }
}
