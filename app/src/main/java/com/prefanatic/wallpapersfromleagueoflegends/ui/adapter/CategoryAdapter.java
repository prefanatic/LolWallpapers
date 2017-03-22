package com.prefanatic.wallpapersfromleagueoflegends.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAssetGroupType;
import com.prefanatic.wallpapersfromleagueoflegends.ui.AssetGroupListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui (Cody Goldberg - 3/30/2016)
 */
public class CategoryAdapter extends FragmentPagerAdapter {
    private final List<AbstractAssetGroupType> list = new ArrayList<>();

    public CategoryAdapter(FragmentManager manager) {
        super(manager);
    }

    public void add(AbstractAssetGroupType type) {
        list.add(type);
    }

    @Override
    public Fragment getItem(int position) {
        return AssetGroupListFragment.Companion.newInstance(list.get(position).id);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getNameTranslated();
    }
}
