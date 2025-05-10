package com.anjelitahp0044.expensestracker_assessment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran

@Database(entities = [Pengeluaran::class], version = 1, exportSchema = false)
abstract class PengeluaranDb : RoomDatabase() {

    abstract val dao: PengeluaranDao

    companion object {

        @Volatile
        private var INSTANCE: PengeluaranDb? = null

        fun getInstance(context: Context): PengeluaranDb {
            return INSTANCE ?: synchronized(this) {
                var instance = INSTANCE
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PengeluaranDb::class.java,
                    "pengeluaran.db"
                )
                    .addMigrations(MIGRATION_DB)
                    .build()
                INSTANCE = instance
                instance
            }
        }
               private val MIGRATION_DB = object : Migration(1, 2) {
                   override fun migrate(db: SupportSQLiteDatabase) {
//                       database.execSQL("ALTER TABLE pengeluaran ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
//                       database.execSQL("ALTER TABLE pengeluaran ADD COLUMN deletedAt TEXT")
                   }
            }
        }
    }
