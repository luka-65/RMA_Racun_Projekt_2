package org.unizd.rma.sarlija.presentation.state

import org.unizd.rma.sarlija.domain.model.Racun

data class RacunUiState(
    val isLoading: Boolean = false,
    val racuni: List<Racun> = emptyList(),
    val error: String? = null
)
