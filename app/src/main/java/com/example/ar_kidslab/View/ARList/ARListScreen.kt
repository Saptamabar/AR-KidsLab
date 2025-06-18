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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
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
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberScene
import io.github.sceneview.rememberView

@Composable
fun ARListScreen(modifier: Modifier = Modifier) {
    // Engine dan loader
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val environmentLoader = rememberEnvironmentLoader(engine)

    // Daftar model dari assets/models/
    val modelList = listOf("Ayam.glb", "Anjing.glb", "Rusa.glb")
    var selectedModel by remember { mutableStateOf(modelList.first()) }

    // Simpan model instance yang aktif
    var modelInstance by remember { mutableStateOf<ModelInstance?>(null) }

    val centerNode = rememberNode(engine)



    // Muat model setiap kali pilihan berubah
    LaunchedEffect(selectedModel) {
        val modelPath = "models/$selectedModel"
        modelInstance = modelLoader.createModelInstance(modelPath)
    }

    // UI: Tombol pilih model
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            modelList.forEach { modelName ->
                Button(onClick = { selectedModel = modelName }) {
                    Text(text = modelName.removeSuffix(".glb"))
                }
            }
        }

        // SceneView
        Box(modifier = Modifier.weight(1f)) {
            Scene(

                modifier = Modifier.fillMaxSize().background(color = Color.Blue),
                engine = engine,

                // Core rendering components
                view = rememberView(engine),
                renderer = rememberRenderer(engine),
                scene = rememberScene(engine),

                // Asset loaders
                modelLoader = modelLoader,
                materialLoader = materialLoader,
                environmentLoader = environmentLoader,

                // Kamera dan kontrol
                cameraNode = rememberCameraNode(engine) {
                    position = Position(z = -4.0f, y = 1.0f)
                    lookAt(centerNode)
                },

                        // Model aktif yang sedang dipilih
                childNodes = remember(modelInstance) {
                    listOfNotNull(
                        modelInstance?.let {
                            ModelNode(
                                modelInstance = it,
                                scaleToUnits = 1.0f
                            )
                        }
                    )
                },



                // Frame event
                onFrame = { /* bisa tambahkan animasi di sini */ }
            )
        }
    }
}
