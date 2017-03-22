package com.prefanatic.wallpapersfromleagueoflegends

import android.content.Context
import android.support.v4.app.Fragment
import com.prefanatic.wallpapersfromleagueoflegends.data.AssetProvider
import com.prefanatic.wallpapersfromleagueoflegends.data.AssetProviderImpl
import com.prefanatic.wallpapersfromleagueoflegends.util.Diary
import timber.log.Timber

/**
 * com.prefanatic.wallpapersfromleagueoflegends (Cody Goldberg - 3/30/2016)
 */
class Application : android.app.Application() {
    lateinit var assetProvider: AssetProvider
        private set

    override fun onCreate() {
        super.onCreate()

        assetProvider = AssetProviderImpl(this)

        Timber.plant(Timber.DebugTree())
        Diary.Page.Builder()
                .setPen(Diary.AndroidPen())
                .setStyle(Diary.AndroidStyle())
                .withCallingClass(true)
                .addToDiary()
    }

    companion object {

        @JvmStatic
        operator fun get(context: Context): Application {
            return context.applicationContext as Application
        }

        @JvmStatic
        operator fun get(fragment: Fragment): Application {
            return fragment.activity.application as Application
        }
    }
}
