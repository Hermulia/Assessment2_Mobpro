package com.anjelitahp0044.expensestracker_assessment2.screen

import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.anjelitahp0044.expensestracker_assessment2.R
import com.anjelitahp0044.expensestracker_assessment2.database.PengeluaranDb
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran

const val KEY_ID_PENGELUARAN = "idPengeluaran"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    navController: NavHostController,
    id: Long? = null,
    isDarkTheme: MutableState<Boolean>
) {
    val context = LocalContext.current
    val db = PengeluaranDb.getInstance(context)
    val factory = com.anjelitahp0044.expensestracker_assessment2.util.ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    val snackbarHostState = remember { SnackbarHostState() }
    var itemToDelete by remember { mutableStateOf<Pengeluaran?>(null) }

    var deskripsi by remember { mutableStateOf("") }
    var nominal by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var pengeluaran by remember { mutableStateOf<Pengeluaran?>(null) }

    LaunchedEffect(true) {
        if (id != null) {
            val data = viewModel.getPengeluaran(id)
            pengeluaran = data
            data?.let {
                deskripsi = it.deskripsi
                nominal = it.nominal.toString()
                kategori = it.kategori
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = Color(0xFF6A1B9A)
                        )
                    }
                },
                title = {
                    Text(
                        text = if (id == null)
                            stringResource(R.string.tambah_pengeluaran)
                        else
                            stringResource(R.string.edit_pengeluaran),
                        color = Color(0xFF6A1B9A)
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFE1BEE7),
                    titleContentColor = Color(0xFF6A1B9A),
                ),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("🌞 / 🌙")
                        Switch(
                            checked = isDarkTheme.value,
                            onCheckedChange = { isDarkTheme.value = it }
                        )
                    }

                    IconButton(onClick = {
                        if (deskripsi.isBlank() || deskripsi.isBlank() || kategori.isBlank()) {
                            Toast.makeText(context, R.string.isi_dulu, Toast.LENGTH_LONG).show()
                            return@IconButton
                        }
                        if (id == null) {
                            viewModel.insert(deskripsi, nominal, kategori)
                        } else {
                            viewModel.update(id, deskripsi, nominal, kategori)
                        }
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = Color(0xFF6A1B9A)
                        )
                    }

                    if (id != null) {
                        DeleteAction {
                            showDialog = true
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormCatatanGabungan(
            deskripsi = deskripsi,
            onDeskripsiChange = { deskripsi = it },
            nominal = nominal,
            onNominalChange = { nominal = it },
            kategori = kategori,
            onKategoriChange = { kategori = it },
            modifier = Modifier.padding(padding)
        )

        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = { showDialog = false }) {
                showDialog = false
                pengeluaran?.let {
                    itemToDelete = Pengeluaran(id = id, deskripsi = deskripsi, nominal = nominal, kategori = kategori, tanggal = it.tanggal)
                }
                viewModel.delete(id)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun DeleteAction(content: () -> Unit) {

}

@Composable
fun FormCatatanGabungan(
    deskripsi: String,
    onDeskripsiChange: (String) -> Unit,
    nominal: String,
    onNominalChange: (String) -> Unit,
    kategori: String,
    onKategoriChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val moodOptions = listOf("Senang", "Sedih", "Kesal", "Marah")
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = deskripsi,
            onValueChange = onDeskripsiChange,
            label = { Text(text = stringResource(R.string.description)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = stringResource(R.string.nominal_pengeluaran),
            color = Color(0xFF6A1B9A),
            style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            moodOptions.forEach { kategori ->
                Button(
                    onClick = { onKategoriChange(kategori) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (kategori == kategori) Color(0xFFFFB6C1) else Color(0xFFFCE4EC)
                    ),
                    shape = RoundedCornerShape(50),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(text = kategori)
                }
            }
        }

        OutlinedTextField(
            value = deskripsi,
            onValueChange = onDeskripsiChange,
            label = { Text(text = stringResource(R.string.isi_dulu)) },
            modifier = Modifier.fillMaxSize()
        )
    }
}
