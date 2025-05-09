package com.anjelitahp0044.expensestracker_assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pengeluaran")
data class Pengeluaran(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val deskripsi: String,
    val nominal: Double,
    val kategori: String,
    val tanggal: String,
    val isDeleted: Boolean = false,
    val deletedAt: String? = null
)