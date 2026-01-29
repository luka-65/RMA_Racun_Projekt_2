package org.unizd.rma.sarlija.domain.usecase

import org.unizd.rma.sarlija.domain.repository.RacunRepository

class GetRacuniUseCase(private val repo: RacunRepository) {
    suspend operator fun invoke() = repo.getAll()
}
