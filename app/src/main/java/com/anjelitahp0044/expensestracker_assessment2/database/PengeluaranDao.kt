package com.anjelitahp0044.expensestracker_assessment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran
import kotlinx.coroutines.flow.Flow
import java.sql.Timestamp

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

    @Query("UPDATE pengeluaran SET isDeleted = 1, deletedAt = :timestamp WHERE id = :id")
    suspend fun softDeleteById(id: Long, timestamp: String)

    @Query("UPDATE pengeluaran SET isDeleted = 0, deletedAt = null WHERE id = :id")
    suspend fun restoreById(id: Long)

    @Query("SELECT * FROM pengeluaran WHERE isDeleted = 1 ORDER BY deletedAt DESC")
    fun getDeletedItems(): Flow<List<Pengeluaran>>

    @Query("DELETE FROM pengeluaran WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pengeluaran WHERE isDeleted = 1 AND deletedAt < :expiredTime")
    suspend fun permanentDeleteExpired(expiredTime: String)
}