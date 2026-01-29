package org.unizd.rma.sarlija.domain.model

data class Racun(
    val id: Long = 0L,
    val nazivTrgovine: String,
    val napomena: String,
    val slika: String,
    val kategorija: Kategorija,
    val datumKupnje: String
)
