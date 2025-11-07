package com.hcpark.news.app.initializer

import android.content.Context
import androidx.startup.Initializer
import com.hcpark.news.app.BuildConfig
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class NapierInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        if (BuildConfig.DEBUG)
            Napier.base(DebugAntilog())
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}