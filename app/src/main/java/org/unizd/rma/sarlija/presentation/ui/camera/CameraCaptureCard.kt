package org.unizd.rma.sarlija.presentation.ui.camera

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun CameraCaptureCard(
    controller: CameraController,
    capturedUri: Uri?,
    onCaptured: (Uri) -> Unit,
    onRemove: () -> Unit,
    onPhotoSavedMessage: () -> Unit,
    onPhotoErrorMessage: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var hasPermission by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Fotografija računa (kamera)", style = MaterialTheme.typography.titleMedium)

            if (!hasPermission) {
                Text(
                    "Nemaš dopuštenje za kameru.",
                    color = MaterialTheme.colorScheme.error
                )
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Zatraži dopuštenje")
                }
                return@Column
            }

            AndroidView(
                factory = { ctx ->
                    PreviewView(ctx).also { previewView ->
                        controller.bind(ctx, previewView, lifecycleOwner)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    controller.takePicture(
                        context = context,
                        onPhotoSaved = { uri ->
                            onCaptured(uri)
                            onPhotoSavedMessage()
                        },
                        onError = {
                            onPhotoErrorMessage()
                        }
                    )
                }) {
                    Text("Uslikaj")
                }

                OutlinedButton(
                    onClick = onRemove,
                    enabled = capturedUri != null
                ) {
                    Text("Ukloni")
                }
            }
        }
    }
}
