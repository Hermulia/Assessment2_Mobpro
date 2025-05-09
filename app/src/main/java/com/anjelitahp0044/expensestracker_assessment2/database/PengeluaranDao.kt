package com.anjelitahp0044.expensestracker_assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran
import kotlinx.coroutines.flow.Flow

@Dao
interface PengeluaranDao {

    @Insert
    suspend fun insert(pengeluaran: Pengeluaran)

    @Update
    suspend fun update(pengeluaran: Pengeluaran)

    @Query("SELECT * FROM pengeluaran WHERE isDeleted = 0 ORDER BY tanggal DESC")
    fun getPengeluaran(): Flow<List<Pengeluaran>>

    @Query("SELECT * FROM pengeluaran WHERE id = :id")
    suspend fun getPengeluaranById(id: Long): Pengeluaran?

    @Query("UPDATE pengeluaran SET isDeleted = 1, deletedAt = :deletedAt WHERE id = :id")
    suspend fun softDeleteById(id: Long, deletedAt: String)

    @Query("SELECT * FROM pengeluaran WHERE isDeleted = 1 ORDER BY deletedAt DESC")
    fun getDeletedPengeluaran(): Flow<List<Pengeluaran>>

    @Query("DELETE FROM pengeluaran WHERE id = :id")
    suspend fun deleteById(id: Long)
}