package org.unizd.rma.sarlija.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onSavedClick: () -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        topBar = { TopAppBar(title = { Text("Upravljaj računima") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Početna", style = MaterialTheme.typography.headlineSmall)

            Button(
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Dodaj račun") }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onSavedClick,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Spremljeni računi") }
        }
    }
}
