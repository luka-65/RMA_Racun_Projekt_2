package org.unizd.rma.sarlija.presentation.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.unizd.rma.sarlija.domain.model.Kategorija
import org.unizd.rma.sarlija.domain.model.Racun
import org.unizd.rma.sarlija.presentation.ui.camera.CameraController
import org.unizd.rma.sarlija.presentation.state.UiEvent
import org.unizd.rma.sarlija.presentation.ui.components.DateField
import org.unizd.rma.sarlija.presentation.ui.components.KategorijaDropdown
import org.unizd.rma.sarlija.presentation.viewmodel.RacunViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.launch
import org.unizd.rma.sarlija.presentation.ui.camera.CameraCaptureCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacunAddScreen(
    vm: RacunViewModel,
    onBack: () -> Unit
) {
    var nazivTrgovine by remember { mutableStateOf("") }
    var napomena by remember { mutableStateOf("") }
    var kategorija by remember { mutableStateOf(Kategorija.HRANA) }
    var datumKupnje by remember { mutableStateOf(todayAsYyyyMmDd()) }
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    val scope = rememberCoroutineScope()


    val cameraController = remember { CameraController() }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    val canSave = nazivTrgovine.isNotBlank() &&
            napomena.isNotBlank() &&
            photoUri != null &&
            datumKupnje.isNotBlank()

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            TopAppBar(
                title = { Text("Dodaj račun") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Nazad"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nazivTrgovine,
                onValueChange = { nazivTrgovine = it },
                label = { Text("Naziv trgovine") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = napomena,
                onValueChange = { napomena = it },
                label = { Text("Napomena") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            KategorijaDropdown(
                selected = kategorija,
                onSelected = { kategorija = it },
                modifier = Modifier.fillMaxWidth()
            )

            DateField(
                label = "Datum kupnje",
                value = datumKupnje,
                onValueChanged = { datumKupnje = it },
                modifier = Modifier.fillMaxWidth()
            )

            CameraCaptureCard(
                controller = cameraController,
                capturedUri = photoUri,
                onCaptured = { photoUri = it },
                onRemove = { photoUri = null },
                onPhotoSavedMessage = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Slika spremljena.")
                    }
                },
                onPhotoErrorMessage = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Greška pri slikanju.")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )



            Spacer(Modifier.height(6.dp))

            Button(
                onClick = {
                    val racun = Racun(
                        nazivTrgovine = nazivTrgovine.trim(),
                        napomena = napomena.trim(),
                        slika = photoUri?.toString().orEmpty(),
                        kategorija = kategorija,
                        datumKupnje = datumKupnje
                    )
                    vm.add(racun)
                    if (canSave) onBack()
                },
                enabled = canSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Spremi račun")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

private fun todayAsYyyyMmDd(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
