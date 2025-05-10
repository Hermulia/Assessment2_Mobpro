package com.anjelitahp0044.expensestracker_assessment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import com.anjelitahp0044.expensestracker_assessment2.navigation.SetupNavGraph
import com.anjelitahp0044.expensestracker_assessment2.ui.theme.ExpensesTracker_Assessment2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val isDarkTheme = rememberSaveable { mutableStateOf(false) }

            ExpensesTracker_Assessment2Theme (
                darkTheme = isDarkTheme.value) {
                SetupNavGraph(navController, isDarkTheme)
            }
        }
    }
}