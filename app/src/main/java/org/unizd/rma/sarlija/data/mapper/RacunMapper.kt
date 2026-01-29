package org.unizd.rma.sarlija.data.mapper

import org.unizd.rma.sarlija.data.local.entity.RacunEntity
import org.unizd.rma.sarlija.domain.model.Kategorija
import org.unizd.rma.sarlija.domain.model.Racun


fun RacunEntity.toDomain(): Racun =
    Racun(
        id = id,
        nazivTrgovine = nazivTrgovine,
        napomena = napomena,
        slika = slika,
        kategorija = Kategorija.valueOf(kategorija),
        datumKupnje = datumKupnje
    )

fun Racun.toEntity(): RacunEntity =
    RacunEntity(
        id = id,
        nazivTrgovine = nazivTrgovine,
        napomena = napomena,
        slika = slika,
        kategorija = kategorija.name,
        datumKupnje = datumKupnje
    )