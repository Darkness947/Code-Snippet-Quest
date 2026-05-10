package com.vigilante.codesnippetquest.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vigilante.codesnippetquest.LocalAppSettings
import com.vigilante.codesnippetquest.R
import java.util.Locale

@Composable
fun SettingsScreen(
    userId: Int,
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onLanguageChanged: () -> Unit
) {
    val context = LocalContext.current
    val appSettings = LocalAppSettings.current
    val prefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    val currentLang = prefs.getString("My_Lang", "en") ?: "en"
    var isArabic by remember { mutableStateOf(currentLang == "ar") }

    var isDarkMode by remember { mutableStateOf(appSettings.isDarkMode.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.screen_background))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .padding(4.dp)
                    .clickable { onNavigateBack() }
            )
            Text(
                text = stringResource(id = R.string.settings),
                color = colorResource(id = R.color.title_color),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        // Settings items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            // Language
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_language),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = stringResource(id = R.string.language),
                            color = colorResource(id = R.color.text_primary),
                            fontSize = 16.sp
                        )
                        Text(
                            text = stringResource(id = R.string.english_arabic),
                            color = colorResource(id = R.color.text_secondary),
                            fontSize = 12.sp
                        )
                    }
                }
                Switch(
                    checked = isArabic,
                    onCheckedChange = {
                        isArabic = it
                        val newLang = if (it) "ar" else "en"
                        prefs.edit().putString("My_Lang", newLang).apply()
                        // Update the reactive locale — triggers instant recomposition
                        appSettings.locale.value = Locale(newLang)
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colorResource(id = R.color.orange_primary),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = colorResource(id = R.color.divider_color)
                    )
                )
            }

            // Dark Mode
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dark_mode),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = stringResource(id = R.string.dark_mode),
                        color = colorResource(id = R.color.text_primary),
                        fontSize = 16.sp
                    )
                }
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = {
                        isDarkMode = it
                        prefs.edit().putBoolean("DarkMode", it).apply()
                        // Update the reactive dark mode state — triggers instant recomposition
                        appSettings.isDarkMode.value = it
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = colorResource(id = R.color.orange_primary),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = colorResource(id = R.color.divider_color)
                    )
                )
            }

            // Logout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable {
                        val userPrefs = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                        userPrefs.edit().clear().apply()
                        onLogout()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(id = R.string.logout),
                    color = colorResource(id = R.color.text_primary),
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        var showClearDialog by remember { mutableStateOf(false) }

        if (showClearDialog) {
            val outerContext = LocalContext.current
            val outerConfig = android.content.res.Configuration(outerContext.resources.configuration)
            val outerLayoutDir = androidx.compose.ui.platform.LocalLayoutDirection.current
            
            androidx.compose.material3.AlertDialog(
                onDismissRequest = { showClearDialog = false },
                title = {
                    CompositionLocalProvider(
                        androidx.compose.ui.platform.LocalContext provides outerContext,
                        androidx.compose.ui.platform.LocalConfiguration provides outerConfig,
                        androidx.compose.ui.platform.LocalLayoutDirection provides outerLayoutDir
                    ) {
                        Text(text = stringResource(id = R.string.clear_score_history))
                    }
                },
                text = {
                    CompositionLocalProvider(
                        androidx.compose.ui.platform.LocalContext provides outerContext,
                        androidx.compose.ui.platform.LocalConfiguration provides outerConfig,
                        androidx.compose.ui.platform.LocalLayoutDirection provides outerLayoutDir
                    ) {
                        Text(text = stringResource(id = R.string.clear_history_confirm_message))
                    }
                },
                confirmButton = {
                    androidx.compose.material3.TextButton(onClick = {
                        viewModel.clearHistory(userId)
                        Toast.makeText(context, context.getString(R.string.history_cleared), Toast.LENGTH_SHORT).show()
                        showClearDialog = false
                    }) {
                        CompositionLocalProvider(
                            androidx.compose.ui.platform.LocalContext provides outerContext,
                            androidx.compose.ui.platform.LocalConfiguration provides outerConfig,
                            androidx.compose.ui.platform.LocalLayoutDirection provides outerLayoutDir
                        ) {
                            Text(text = stringResource(id = R.string.clear_history_confirm_yes), color = colorResource(id = R.color.red_fail))
                        }
                    }
                },
                dismissButton = {
                    androidx.compose.material3.TextButton(onClick = { showClearDialog = false }) {
                        CompositionLocalProvider(
                            androidx.compose.ui.platform.LocalContext provides outerContext,
                            androidx.compose.ui.platform.LocalConfiguration provides outerConfig,
                            androidx.compose.ui.platform.LocalLayoutDirection provides outerLayoutDir
                        ) {
                            Text(text = stringResource(id = R.string.clear_history_confirm_no), color = colorResource(id = R.color.text_primary))
                        }
                    }
                },
                containerColor = colorResource(id = R.color.card_background),
                titleContentColor = colorResource(id = R.color.title_color),
                textContentColor = colorResource(id = R.color.text_primary)
            )
        }

        // Clear History
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 48.dp)
                .clickable {
                    showClearDialog = true
                },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.clear_score_history),
                color = colorResource(id = R.color.red_fail),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
