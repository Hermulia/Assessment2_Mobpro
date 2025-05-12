package com.anjelitahp0044.expensestracker_assessment2.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.anjelitahp0044.expensestracker_assessment2.R
import com.anjelitahp0044.expensestracker_assessment2.database.PengeluaranDb
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran
import com.anjelitahp0044.expensestracker_assessment2.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(
    navController: NavHostController,
    isDarkTheme: MutableState<Boolean>,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory(PengeluaranDb.getInstance(LocalContext.current).dao)),
) {
    val deletedItems by viewModel.getDeletedItems().collectAsState(initial = emptyList())
    var isGridView by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Recycle Bin",
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌞 / 🌙", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Switch(
                            checked = isDarkTheme.value,
                            onCheckedChange = { isDarkTheme.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.secondary
                            )
                        )
                        IconToggleButton(
                            checked = isGridView,
                            onCheckedChange = { isGridView = it }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (isGridView) R.drawable.baseline_view_list_24
                                    else R.drawable.baseline_grid_view_24
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (deletedItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Kosong", color = MaterialTheme.colorScheme.onBackground)
            }
        } else {
            if (isGridView) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(160.dp),
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(deletedItems, key = { it.id }) { item ->
                        DeletedItemCard(item, { viewModel.restore(item.id) }, { viewModel.delete(item.id) })
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                ) {
                    items(deletedItems, key = { it.id }) { item ->
                        DeletedItemCard(item, { viewModel.restore(item.id) }, { viewModel.delete(item.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun DeletedItemCard(
    item: Pengeluaran,
    onRestore: () -> Unit,
    onPermanentDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Nominal: ${item.nominal}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(item.deskripsi, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Kategori: ${item.kategori}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Dihapus: ${item.deletedAt ?: "-"}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onRestore) {
                    Text("Restore", color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onPermanentDelete) {
                    Text("Hapus Permanen", color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
