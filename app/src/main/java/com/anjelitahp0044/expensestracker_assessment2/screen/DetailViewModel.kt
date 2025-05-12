package com.anjelitahp0044.expensestracker_assessment2.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anjelitahp0044.expensestracker_assessment2.database.PengeluaranDao
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailViewModel(private val dao: PengeluaranDao) : ViewModel() {

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun insert(deskripsi: String, nominal: String, kategori: String) {
        val pengeluaran = Pengeluaran(
            deskripsi = deskripsi,
            nominal = nominal.toString(),
            kategori = kategori,
            tanggal = formatter.format(Date())
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(pengeluaran)
        }
    }

    suspend fun getPengeluaran(id: Long): Pengeluaran? = dao.getPengeluaranById(id)

    fun update(id: Long, deskripsi: String, nominal: String, kategori: String) {
        val pengeluaran = Pengeluaran(
            id = id,
            deskripsi = deskripsi,
            nominal = nominal.toString(),
            kategori = kategori,
            tanggal = formatter.format(Date())
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(pengeluaran)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }

    fun restore(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.restoreById(id)
        }
    }

    fun getDeletedItems(): Flow<List<Pengeluaran>> = dao.getDeletedItems()

}