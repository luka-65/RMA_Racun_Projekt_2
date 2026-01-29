package org.unizd.rma.sarlija.domain.repository

import org.unizd.rma.sarlija.domain.model.Racun

interface RacunRepository {

    suspend fun getAll(): List<Racun>
    suspend fun insert(racun: Racun)
    suspend fun delete(racun: Racun)
}