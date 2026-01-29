package org.unizd.rma.sarlija.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.unizd.rma.sarlija.domain.model.Racun
import org.unizd.rma.sarlija.domain.usecase.AddRacunUseCase
import org.unizd.rma.sarlija.domain.usecase.DeleteRacunUseCase
import org.unizd.rma.sarlija.domain.usecase.GetRacuniUseCase
import org.unizd.rma.sarlija.presentation.state.RacunUiState
import org.unizd.rma.sarlija.presentation.state.UiEvent

class RacunViewModel(
    private val addRacun: AddRacunUseCase,
    private val getRacuni: GetRacuniUseCase,
    private val deleteRacun: DeleteRacunUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RacunUiState())
    val uiState: StateFlow<RacunUiState> = _uiState.asStateFlow()

    private val _events = Channel<UiEvent>(Channel.BUFFERED)
    val events: Flow<UiEvent> = _events.receiveAsFlow()

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { getRacuni() }
                .onSuccess { list ->
                    _uiState.update { it.copy(isLoading = false, racuni = list) }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Greška") }
                    _events.send(UiEvent.ShowSnackbar("Greška kod učitavanja računa."))
                }
        }
    }

    fun add(racun: Racun) {
        viewModelScope.launch {
            val msg = validate(racun)
            if (msg != null) {
                _events.send(UiEvent.ShowSnackbar(msg))
                return@launch
            }

            runCatching { addRacun(racun) }
                .onSuccess {
                    _events.send(UiEvent.ShowSnackbar("Račun je spremljen."))
                    load()
                }
                .onFailure {
                    _events.send(UiEvent.ShowSnackbar("Spremanje nije uspjelo."))
                }
        }
    }

    fun delete(racun: Racun) {
        viewModelScope.launch {
            runCatching { deleteRacun(racun) }
                .onSuccess {
                    _events.send(UiEvent.ShowSnackbar("Račun je obrisan."))
                    load()
                }
                .onFailure {
                    _events.send(UiEvent.ShowSnackbar("Brisanje nije uspjelo."))
                }
        }
    }

    private fun validate(r: Racun): String? {
        if (r.nazivTrgovine.isBlank()) return "Naziv trgovine je obavezan."
        if (r.napomena.isBlank()) return "Napomena je obavezna."
        if (r.slika.isBlank()) return "Moraš uslikati račun."
        if (!Regex("""\d{4}-\d{2}-\d{2}""").matches(r.datumKupnje)) return "Datum mora biti u formatu yyyy-MM-dd."
        return null
    }
}
