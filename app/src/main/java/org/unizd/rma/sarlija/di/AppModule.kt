package org.unizd.rma.sarlija.di

import android.content.Context
import androidx.room.Room
import org.unizd.rma.sarlija.data.local.AppDatabase
import org.unizd.rma.sarlija.data.repository.RacunRepositoryImpl
import org.unizd.rma.sarlija.domain.repository.RacunRepository
import org.unizd.rma.sarlija.domain.usecase.AddRacunUseCase
import org.unizd.rma.sarlija.domain.usecase.DeleteRacunUseCase
import org.unizd.rma.sarlija.domain.usecase.GetRacuniUseCase

object AppModule {

    @Volatile private var db: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        return db ?: synchronized(this) {
            db ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "racun_db"
            ).build().also { db = it }
        }
    }

    fun provideRepository(context: Context): RacunRepository {
        val database = provideDatabase(context)
        return RacunRepositoryImpl(database.racunDao())
    }

    fun provideAddUseCase(context: Context) =
        AddRacunUseCase(provideRepository(context))

    fun provideGetUseCase(context: Context) =
        GetRacuniUseCase(provideRepository(context))

    fun provideDeleteUseCase(context: Context) =
        DeleteRacunUseCase(provideRepository(context))
}
