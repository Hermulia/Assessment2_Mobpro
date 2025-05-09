package com.anjelitahp0044.expensestracker_assessment2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavGraph
import com.anjelitahp0044.expensestracker_assessment2.ui.theme.ExpensesTracker_Assessment2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpensesTracker_Assessment2Theme {
                SetupNavGraph()
            }
        }

    }
}
