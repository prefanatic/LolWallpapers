package com.prefanatic.wallpapersfromleagueoflegends.ui


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prefanatic.wallpapersfromleagueoflegends.Application
import com.prefanatic.wallpapersfromleagueoflegends.R
import com.prefanatic.wallpapersfromleagueoflegends.data.AssetProvider
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAssetGroup
import com.prefanatic.wallpapersfromleagueoflegends.ui.adapter.ItemAdapter
import com.prefanatic.wallpapersfromleagueoflegends.util.Diary
import kotlinx.android.synthetic.main.asset_group_list_fragment.*
import java.util.*

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui (Cody Goldberg - 3/30/2016)
 */
class AssetGroupListFragment : Fragment() {
    private lateinit var adapter: ItemAdapter
    private lateinit var provider: AssetProvider

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.asset_group_list_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments.getString(ID)

        provider = Application[this].assetProvider
        adapter = ItemAdapter()

        adapter.observeClicks(AbstractAssetGroup::class.java)
                .map(ItemAdapter.ClickEvent<AbstractAssetGroup>::data)
                .subscribe { group ->
                    val intent = Intent(activity, AssetActivity::class.java)
                    intent.putStringArrayListExtra("assets", ArrayList(group.assets))

                    startActivity(intent)
                }

        Diary.d(id)
        recycler.layoutManager = GridLayoutManager(activity, 2)
        recycler.adapter = adapter

        provider.assetGroups
                .flatMapIterable({ groups -> groups })
                .cast(AbstractAssetGroup::class.java)
                .filter { group -> group.tags.contains(id) }
                .subscribe { group -> adapter.insert(group) }
    }

    companion object {
        private val ID = "id"

        fun newInstance(id: String): AssetGroupListFragment {
            val fragment = AssetGroupListFragment()
            val bundle = Bundle()

            bundle.putString(ID, id)

            fragment.arguments = bundle
            return fragment
        }
    }
}
