package org.unizd.rma.sarlija.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.unizd.rma.sarlija.domain.usecase.AddRacunUseCase
import org.unizd.rma.sarlija.domain.usecase.DeleteRacunUseCase
import org.unizd.rma.sarlija.domain.usecase.GetRacuniUseCase
import org.unizd.rma.sarlija.presentation.viewmodel.RacunViewModel

class RacunViewModelFactory(
    private val add: AddRacunUseCase,
    private val get: GetRacuniUseCase,
    private val delete: DeleteRacunUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RacunViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RacunViewModel(add, get, delete) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}