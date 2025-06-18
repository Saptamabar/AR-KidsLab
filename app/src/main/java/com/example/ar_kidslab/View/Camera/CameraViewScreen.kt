package com.example.ar_kidslab.View.Camera

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CameraViewScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner

    val previewUseCase = remember { Preview.Builder().build() }
    val imageCaptureUseCase = remember {
        ImageCapture.Builder()
            .setTargetRotation(android.view.Surface.ROTATION_0)
            .build()
    }

    var cameraProvider by remember { mutableStateOf<ProcessCameraProvider?>(null) }

    fun bindCamera() {
        cameraProvider?.let {
            val selector = CameraSelector.DEFAULT_BACK_CAMERA
            it.unbindAll()
            it.bindToLifecycle(
                lifecycleOwner,
                selector,
                previewUseCase,
                imageCaptureUseCase
            )
        }
    }

    LaunchedEffect(Unit) {
        try {
            cameraProvider = ProcessCameraProvider.getInstance(context).get()
            bindCamera()
        } catch (e: Exception) {
            Log.e("CameraError", "Error initializing camera: ${e.localizedMessage}")
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            factory = { ctx ->
                PreviewView(ctx).also {
                    previewUseCase.setSurfaceProvider(it.surfaceProvider)
                }
            }
        )

        Button(
            onClick = {
                val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
                    .format(System.currentTimeMillis()) + ".jpg"

                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, name)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-App")
                    }
                }

                val outputOptions = ImageCapture.OutputFileOptions
                    .Builder(
                        context.contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    ).build()

                imageCaptureUseCase.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            Toast.makeText(context, "Photo saved!", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Log.e("CameraX", "Photo capture failed: ${exception.message}", exception)
                            Toast.makeText(context, "Failed to save photo", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Ambil Foto")
        }
    }
}