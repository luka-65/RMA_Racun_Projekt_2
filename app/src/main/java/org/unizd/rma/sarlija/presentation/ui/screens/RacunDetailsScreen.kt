package org.unizd.rma.sarlija.presentation.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.unizd.rma.sarlija.presentation.state.UiEvent
import org.unizd.rma.sarlija.presentation.viewmodel.RacunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacunDetailsScreen(
    vm: RacunViewModel,
    racunId: Long,
    onBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(Unit) { if (state.racuni.isEmpty()) vm.load() }

    val racun = state.racuni.firstOrNull { it.id == racunId }

    val snackbarHostState = remember { SnackbarHostState() }

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                    if (event.message.contains("obrisan", ignoreCase = true)) {
                        onBack()
                    }
                }
            }
        }
    }

    if (showDeleteDialog && racun != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Brisanje računa") },
            text = { Text("Jeste li sigurni da želite obrisati ovaj račun?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    vm.delete(racun)
                }) { Text("Obriši") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Odustani") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            TopAppBar(
                title = { Text("Detalji računa") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Nazad")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (racun == null) {
                Text("Račun nije pronađen.")
                return@Column
            }

            Text(racun.nazivTrgovine, style = MaterialTheme.typography.headlineSmall)
            Text("Kategorija: ${racun.kategorija.name}")
            Text("Datum kupnje: ${racun.datumKupnje}")

            Divider()

            Text("Napomena:", style = MaterialTheme.typography.titleMedium)
            Text(racun.napomena)

            Divider()

            AsyncImage(
                model = Uri.parse(racun.slika),
                contentDescription = "Slika računa",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Obriši račun")
            }
        }
    }
}
