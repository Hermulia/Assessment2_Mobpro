package com.anjelitahp0044.expensestracker_assessment2.navigation

import com.anjelitahp0044.expensestracker_assessment2.screen.KEY_ID_PENGELUARAN

sealed class Screen(val route: String) {
    data object Home : Screen("mainScreen")
    data object FormBaru: Screen("detailScreen")
    data object RecycleBin : Screen("recycleBin")
    data object FormUbah: Screen("detailScreen/{$KEY_ID_PENGELUARAN}") {
        fun withId(id: Long) = "detailScreen/$id"
    }
}