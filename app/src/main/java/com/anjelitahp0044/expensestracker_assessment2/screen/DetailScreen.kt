package com.anjelitahp0044.expensestracker_assessment2.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                title = {
                    Text(
                        text = if (id == null)
                            stringResource(R.string.tambah_pengeluaran)
                        else
                            stringResource(R.string.edit_pengeluaran),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("🌞 / 🌙", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Switch(
                            checked = isDarkTheme.value,
                            onCheckedChange = { isDarkTheme.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                    }

                    IconButton(onClick = {
                        if (deskripsi.isBlank() || nominal.isBlank() || kategori.isBlank()) {
                            Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
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
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
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
    IconButton(onClick = content) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Hapus",
            tint = MaterialTheme.colorScheme.error
        )
    }
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
    val kategoriOptions = listOf(
        "🍝MAKAN", "🚗TRANSPORTASI", "🏠TEMPAT TINGGAL",
        "💳TAGIHAN & UTILITAS", "💊KESEHATAN", "📚PENDIDIKAN",
        "🎇HIBURAN", "💶DONASI/ZAKAT", "💹TABUNGAN & INVESTASI", "⁉️LAINNYA"
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = deskripsi,
            onValueChange = onDeskripsiChange,
            label = { Text(text = stringResource(R.string.description)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        OutlinedTextField(
            value = nominal,
            onValueChange = onNominalChange,
            label = { Text(text = stringResource(R.string.isi_dulu)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        Text(
            text = stringResource(R.string.nominal_pengeluaran),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            kategoriOptions.forEach { kategoriOption ->
                Button(
                    onClick = { onKategoriChange(kategoriOption) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (kategori == kategoriOption)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = kategoriOption)
                }
            }
        }
    }
}
