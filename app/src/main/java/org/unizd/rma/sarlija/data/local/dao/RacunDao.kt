package org.unizd.rma.sarlija.data.local.dao

import androidx.room.*
import org.unizd.rma.sarlija.data.local.entity.RacunEntity

@Dao
interface RacunDao {

    @Query("SELECT * FROM racuni ORDER BY datumKupnje DESC")
    suspend fun getAll(): List<RacunEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(racun: RacunEntity)

    @Delete
    suspend fun delete(racun: RacunEntity)
}