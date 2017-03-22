package com.prefanatic.wallpapersfromleagueoflegends.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prefanatic.wallpapersfromleagueoflegends.Application;
import com.prefanatic.wallpapersfromleagueoflegends.R;
import com.prefanatic.wallpapersfromleagueoflegends.data.AssetProvider;
import com.prefanatic.wallpapersfromleagueoflegends.ui.adapter.AssetListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui (Cody Goldberg - 3/31/2016)
 */
public class AssetListFragment extends Fragment {
    @Bind(R.id.recycler) RecyclerView recyclerView;

    private AssetProvider provider;
    private AssetListAdapter adapter;

    public static AssetListFragment newInstance(ArrayList<String> assets) {
        Bundle args = new Bundle();
        args.putStringArrayList("assets", assets);

        AssetListFragment fragment = new AssetListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.asset_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        provider = Application.get(this).getAssetProvider();
        adapter = new AssetListAdapter();
        recyclerView.setAdapter(adapter);

        ArrayList<String> assetFilter = getArguments().getStringArrayList("assets");
        provider.getAssets()
                .flatMapIterable(list -> list)
                .filter(asset -> assetFilter.contains(asset.id))
                .subscribe(asset -> {
                    adapter.add(asset);
                });

    }
}
