package com.anjelitahp0044.expensestracker_assessment2.database

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran

@Database(entities = [Pengeluaran::class], version = 2)
abstract class PengeluaranDb : RoomDatabase() {
    abstract val dao: PengeluaranDao

    companion object {
        @Volatile
        private var INSTANCE: PengeluaranDb? = null

        fun getInstance(context: Context): PengeluaranDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PengeluaranDb::class.java,
                    "pengeluaran.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE pengeluaran ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE pengeluaran ADD COLUMN deletedAt TEXT")
            }
        }
    }
}