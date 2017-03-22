package com.prefanatic.wallpapersfromleagueoflegends.data;



import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Asset;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AssetGroup;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AssetGroupType;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Locale;

import java.util.List;

import rx.Observable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 3/30/2016)
 */
public interface AssetProvider {

    Observable<List<AssetGroupType>> getAssetGroupTypes();

    Observable<List<AssetGroup>> getAssetGroups();
    Observable<AssetGroup> getAssetGroupById(String id);

    Observable<List<Asset>> getAssets();
    Observable<List<Asset>> getAssetsInGroup(String groupId);
    Observable<Asset> getAssetById(String id);

    Observable<Locale> getLocale();
}
