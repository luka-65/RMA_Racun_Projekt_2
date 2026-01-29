package org.unizd.rma.sarlija.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import org.unizd.rma.sarlija.data.local.dao.RacunDao
import org.unizd.rma.sarlija.data.local.entity.RacunEntity

@Database(
    entities = [RacunEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun racunDao(): RacunDao
}
