package com.anjelitahp0044.expensestracker_assessment2.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavHostController, isDarkTheme: MutableState<Boolean>) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("🗿Tentang Aplikasi") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text("🌞 / 🌙")
                        Switch(
                            checked = isDarkTheme.value,
                            onCheckedChange = { isDarkTheme.value = it }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            Text("Nama Aplikasi :", style = MaterialTheme.typography.titleMedium)
            Text("Expenses Tracker", fontWeight = FontWeight.Bold)

            Text("Dibuat oleh :", style = MaterialTheme.typography.titleMedium)
            Text("Anjelita Hermulia Putri")

            Text("NIM :", style = MaterialTheme.typography.titleMedium)
            Text("607062330044")

            Text("Kelas :", style = MaterialTheme.typography.titleMedium)
            Text("D3IF - 47 - 01")

            Text("Versi :", style = MaterialTheme.typography.titleMedium)
            Text("1.0.0")

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Tema saat ini :", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.width(12.dp))
                Text(if (isDarkTheme.value) "Gelap" else "Terang", fontWeight = FontWeight.Medium)
            }
        }
    }
}
