package org.unizd.rma.sarlija.data.repository

import org.unizd.rma.sarlija.data.local.dao.RacunDao
import org.unizd.rma.sarlija.domain.repository.RacunRepository
import org.unizd.rma.sarlija.data.mapper.toDomain
import org.unizd.rma.sarlija.data.mapper.toEntity
import org.unizd.rma.sarlija.domain.model.Racun

class RacunRepositoryImpl(
    private val dao: RacunDao
) : RacunRepository {

    override suspend fun getAll(): List<Racun> =
        dao.getAll().map { it.toDomain() }

    override suspend fun insert(racun: Racun) =
        dao.insert(racun.toEntity())

    override suspend fun delete(racun: Racun) =
        dao.delete(racun.toEntity())
}