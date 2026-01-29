package org.unizd.rma.sarlija.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racuni")
data class RacunEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nazivTrgovine: String,
    val napomena: String,
    val slika: String,
    val kategorija: String,
    val datumKupnje: String
)