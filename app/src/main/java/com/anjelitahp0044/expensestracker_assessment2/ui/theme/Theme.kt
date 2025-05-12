package com.anjelitahp0044.expensestracker_assessment2.ui.theme

import DeepPlumGray
import DeepPlumGrayDark
import DustyPink
import DustyPinkDark
import DustyRose
import DustyRoseDark
import LavenderDark
import LightLavender
import MauveBrown
import MauveBrownDark
import OrangeDarkSnackbar
import OrangeSnackbar
import PeachCream
import PeachCreamDark
import SoftIndigo
import SoftIndigoDark
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = SoftIndigo,
    onPrimary = DeepPlumGray,
    secondary = LightLavender,
    onSecondary = DeepPlumGray,
    background = PeachCream,
    onBackground = DeepPlumGray,
    surface = DustyRose,
    onSurface = DeepPlumGray,
    tertiary = DustyPink,
    error = OrangeSnackbar,
    outline = MauveBrown
)

private val DarkColors = darkColorScheme(
    primary = SoftIndigoDark,
    onPrimary = DeepPlumGrayDark,
    secondary = LavenderDark,
    onSecondary = DeepPlumGrayDark,
    background = PeachCreamDark,
    onBackground = DeepPlumGrayDark,
    surface = DustyRoseDark,
    onSurface = DeepPlumGrayDark,
    tertiary = DustyPinkDark,
    error = OrangeDarkSnackbar,
    outline = MauveBrownDark
)

@Composable
fun ExpensesTracker_Assessment2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
