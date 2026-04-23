package com.vigilante.codesnippetquest

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.vigilante.codesnippetquest.data.AppDatabase
import java.util.Locale

interface AppContainer {
    val database: AppDatabase
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val database: AppDatabase by lazy {
        AppDatabase.getDatabase(context)
    }
}

class MyApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }

    override fun attachBaseContext(base: Context) {
        val prefs = base.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("My_Lang", "en") ?: "en"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(base.createConfigurationContext(config))
    }
}
