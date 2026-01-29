package org.unizd.rma.sarlija.domain.usecase

import org.unizd.rma.sarlija.domain.model.Racun
import org.unizd.rma.sarlija.domain.repository.RacunRepository

class AddRacunUseCase(private val repo: RacunRepository) {
    suspend operator fun invoke(racun: Racun) = repo.insert(racun)
}