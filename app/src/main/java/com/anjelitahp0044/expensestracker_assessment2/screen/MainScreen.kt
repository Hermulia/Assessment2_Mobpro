package com.anjelitahp0044.expensestracker_assessment2.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.anjelitahp0044.expensestracker_assessment2.R
import com.anjelitahp0044.expensestracker_assessment2.database.PengeluaranDb
import com.anjelitahp0044.expensestracker_assessment2.model.Pengeluaran
import com.anjelitahp0044.expensestracker_assessment2.navigation.Screen
import com.anjelitahp0044.expensestracker_assessment2.ui.theme.ExpensesTracker_Assessment2Theme
import com.anjelitahp0044.expensestracker_assessment2.util.SettingsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController, isDarkTheme: MutableState<Boolean>) {
    val context = LocalContext.current
    val db = PengeluaranDb.getInstance(context)
    val factory = com.anjelitahp0044.expensestracker_assessment2.util.ViewModelFactory(db.dao)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val snackbarHostState = remember { SnackbarHostState() }
    var itemToDelete by remember { mutableStateOf<Pengeluaran?>(null) }
    val dataStore = SettingsDataStore(context)
    val showList by dataStore.layoutFlow.collectAsState(initial = true)
    var showDropUp by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌞 / 🌙", color = MaterialTheme.colorScheme.onPrimary)
                        Switch(
                            checked = isDarkTheme.value,
                            onCheckedChange = { isDarkTheme.value = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        IconButton(
                            onClick = {
                                CoroutineScope(Dispatchers.IO).launch {
                                    dataStore.saveLayout(!showList)
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(
                                    if (showList) R.drawable.baseline_grid_view_24
                                    else R.drawable.baseline_view_list_24
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(bottom = 16.dp, end = 16.dp)
            ) {
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.RecycleBin.route) },
                    containerColor = MaterialTheme.colorScheme.error
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Recycle Bin")
                }
                FloatingActionButton(
                    onClick = { navController.navigate(Screen.FormBaru.route) },
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Tambah")
                }
            }
        },
        bottomBar = {
            Box(Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { showDropUp = !showDropUp },
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp, bottom = 8.dp)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = showDropUp,
                    onDismissRequest = { showDropUp = false },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    DropdownMenuItem(
                        text = { Text("Tentang Aplikasi") },
                        onClick = {
                            showDropUp = false
                            navController.navigate("aboutScreen")
                        }
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        ScreenContent(
            showList = showList,
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            viewModel = viewModel,
            onDelete = {
                viewModel.delete(it.id)
                itemToDelete = it
            }
        )

        itemToDelete?.let { item ->
            LaunchedEffect(item) {
                val result = snackbarHostState.showSnackbar(
                    message = "${item.kategori} dihapus",
                    actionLabel = "UNDO",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.restore(item.id)
                }
                itemToDelete = null
            }
        }
    }
}

@Composable
fun ScreenContent(
    showList: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: MainViewModel,
    onDelete: (Pengeluaran) -> Unit
) {
    val data by viewModel.data.collectAsState()
    if (data.isEmpty()) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(id = R.string.list_kosong),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(it, {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }, { onDelete(it) })
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = modifier.fillMaxSize(),
                verticalItemSpacing = 12.dp,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(12.dp, 12.dp, 12.dp, 100.dp)
            ) {
                items(data) {
                    GridItem(it, {
                        navController.navigate(Screen.FormUbah.withId(it.id))
                    }, { onDelete(it) })
                }
            }
        }
    }
}

@Composable
fun ListItem(pengeluaran: Pengeluaran, onClick: () -> Unit, onDelete: () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            pengeluaran.deskripsi,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(pengeluaran.nominal, color = MaterialTheme.colorScheme.onSurface)
        Text(
            pengeluaran.tanggal,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            "Hapus",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.clickable { onDelete() }
        )
    }
}

@Composable
fun GridItem(pengeluaran: Pengeluaran, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                pengeluaran.deskripsi,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(pengeluaran.nominal, color = MaterialTheme.colorScheme.onSurface)
            Text(
                pengeluaran.tanggal,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Hapus",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable { onDelete() }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewMainScreen() {
    val isDark = remember { mutableStateOf(false) }
    ExpensesTracker_Assessment2Theme(darkTheme = isDark.value) {
        MainScreen(navController = rememberNavController(), isDarkTheme = isDark)
    }
}
