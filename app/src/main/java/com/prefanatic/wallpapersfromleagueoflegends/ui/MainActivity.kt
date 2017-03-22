package com.prefanatic.wallpapersfromleagueoflegends.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.prefanatic.wallpapersfromleagueoflegends.Application
import com.prefanatic.wallpapersfromleagueoflegends.R
import com.prefanatic.wallpapersfromleagueoflegends.data.AssetProvider
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AssetGroupType
import com.prefanatic.wallpapersfromleagueoflegends.ui.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.main_activity.*
import rx.android.schedulers.AndroidSchedulers

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui (Cody Goldberg - 3/30/2016)
 */
class MainActivity : AppCompatActivity() {
    private var provider: AssetProvider? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        provider = Application[this].assetProvider

        // Query the provider to get the tabs.
        provider!!.assetGroupTypes
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { setupCategories(it) }

    }

    private fun setupCategories(types: List<AssetGroupType>) {
        val adapter = CategoryAdapter(supportFragmentManager)

        types.forEach { type -> adapter.add(type) }

        view_pager.adapter = adapter
        tab_layout.setupWithViewPager(view_pager)
    }
}
