package org.unizd.rma.sarlija.presentation.state

sealed class UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent()
}
