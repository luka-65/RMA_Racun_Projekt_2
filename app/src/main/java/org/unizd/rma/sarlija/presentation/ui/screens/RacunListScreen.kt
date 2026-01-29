package org.unizd.rma.sarlija.presentation.ui.screens

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.unizd.rma.sarlija.domain.model.Racun
import org.unizd.rma.sarlija.presentation.state.UiEvent
import org.unizd.rma.sarlija.presentation.viewmodel.RacunViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RacunListScreen(
    vm: RacunViewModel,
    onOpenDetails: (Long) -> Unit,
    onBack: () -> Unit
) {
    val state by vm.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        vm.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    LaunchedEffect(Unit) { vm.load() }

    var toDelete by remember { mutableStateOf<Racun?>(null) }

    if (toDelete != null) {
        AlertDialog(
            onDismissRequest = { toDelete = null },
            title = { Text("Brisanje računa") },
            text = { Text("Jeste li sigurni da želite obrisati ovaj račun?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.delete(toDelete!!)
                    toDelete = null
                }) { Text("Obriši") }
            },
            dismissButton = {
                TextButton(onClick = { toDelete = null }) { Text("Odustani") }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = {
            TopAppBar(
                title = { Text("Spremljeni računi") },
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
                .padding(16.dp)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
                Spacer(Modifier.height(12.dp))
            }

            if (state.racuni.isEmpty() && !state.isLoading && state.error == null) {
                Text("Nema spremljenih računa.")
                Spacer(Modifier.height(12.dp))
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.racuni) { racun ->
                    RacunCard(
                        racun = racun,
                        onOpen = { onOpenDetails(racun.id) },
                        onDeleteClick = { toDelete = racun }
                    )
                }
            }
        }
    }
}

@Composable
private fun RacunCard(
    racun: Racun,
    onOpen: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        Modifier
            .fillMaxWidth()
            .clickable { onOpen() }
    ) {
        Row(
            Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = Uri.parse(racun.slika),
                contentDescription = "Slika računa",
                modifier = Modifier
                    .width(92.dp)
                    .height(92.dp)
            )

            Column(Modifier.weight(1f)) {
                Text(racun.nazivTrgovine, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(4.dp))
                Text("Kategorija: ${racun.kategorija.name}")
                Text("Datum: ${racun.datumKupnje}")
            }

            TextButton(onClick = onDeleteClick) { Text("Obriši") }
        }
    }
}
