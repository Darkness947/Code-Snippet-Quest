package com.vigilante.codesnippetquest

import android.content.Context
//import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.vigilante.codesnippetquest.ui.ViewModelFactory
import com.vigilante.codesnippetquest.ui.navigation.AppNavigation
import com.vigilante.codesnippetquest.ui.theme.CodeSnippetQuestTheme
import java.util.Locale

// Global reactive state for dark mode and locale
data class AppSettings(
    val isDarkMode: MutableState<Boolean>,
    val locale: MutableState<Locale>
)

val LocalAppSettings = staticCompositionLocalOf<AppSettings> {
    error("AppSettings not provided")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as MyApplication
        val factory = ViewModelFactory(app)

        val prefs = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("DarkMode", false)
        val lang = prefs.getString("My_Lang", "en") ?: "en"

        val appSettings = AppSettings(
            isDarkMode = mutableStateOf(isDark),
            locale = mutableStateOf(Locale(lang))
        )

        setContent {
            CompositionLocalProvider(LocalAppSettings provides appSettings) {
                LocalizedContent(appSettings = appSettings, factory = factory)
            }
        }
    }
}

@Composable
fun LocalizedContent(appSettings: AppSettings, factory: ViewModelFactory) {
    val locale = appSettings.locale.value
    val context = LocalContext.current

    // Update system default locale so non-Compose logic picks it up
    java.util.Locale.setDefault(locale)

    // Create a localized configuration context
    val config = android.content.res.Configuration(context.resources.configuration).apply {
        setLocale(locale)
        setLayoutDirection(locale)
        // Ensure the context knows if it's dark or light mode for XML resource loading
        val uiMode = if (appSettings.isDarkMode.value) {
            android.content.res.Configuration.UI_MODE_NIGHT_YES
        } else {
            android.content.res.Configuration.UI_MODE_NIGHT_NO
        }
        this.uiMode = (this.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK.inv()) or uiMode
    }
    val localizedContext = context.createConfigurationContext(config)

    val layoutDirection = if (androidx.core.text.TextUtilsCompat.getLayoutDirectionFromLocale(locale) == android.view.View.LAYOUT_DIRECTION_RTL) {
        androidx.compose.ui.unit.LayoutDirection.Rtl
    } else {
        androidx.compose.ui.unit.LayoutDirection.Ltr
    }

    CompositionLocalProvider(
        androidx.compose.ui.platform.LocalContext provides localizedContext,
        androidx.compose.ui.platform.LocalLayoutDirection provides layoutDirection
    ) {
        CodeSnippetQuestTheme(
            darkTheme = appSettings.isDarkMode.value,
            dynamicColor = false
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AppNavigation(viewModelFactory = factory)
            }
        }
    }
}
