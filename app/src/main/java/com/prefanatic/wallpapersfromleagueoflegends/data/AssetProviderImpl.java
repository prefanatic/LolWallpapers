package com.prefanatic.wallpapersfromleagueoflegends.data;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prefanatic.wallpapersfromleagueoflegends.BuildConfig;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAsset;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAssetGroup;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAssetGroupType;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractData;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractLocale;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Asset;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AssetGroup;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AssetGroupType;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Data;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Locale;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Models;
import com.prefanatic.wallpapersfromleagueoflegends.util.Diary;
import com.prefanatic.wallpapersfromleagueoflegends.util.TranslateUtil;

import java.util.List;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.rx.RxSupport;
import io.requery.rx.SingleEntityStore;
import io.requery.sql.Configuration;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data (Cody Goldberg - 3/30/2016)
 */
public class AssetProviderImpl implements AssetProvider {
    private static final String URL = "http://screensaver.riotgames.com/latest/content/data.json";

    private final OkHttpClient client;
    private final ObjectMapper mapper;
    private final SingleEntityStore<Persistable> dataStore;

    private Data data;

    public AssetProviderImpl(Context context) {
        client = new OkHttpClient();
        mapper = new ObjectMapper();

        // Create our SQL database.
        DatabaseSource source = new DatabaseSource(context, Models.DEFAULT, 1);

        if (BuildConfig.DEBUG) {
            source.setTableCreationMode(TableCreationMode.DROP_CREATE);
        }

        Configuration configuration = source.getConfiguration();
        dataStore = RxSupport.toReactiveStore(
                new EntityDataStore<Persistable>(configuration)
        );
    }

    private Observable<Data> fetchData() {
        Observable<Data> memory = Observable.just(data)
                .doOnNext(result -> Diary.d("From Memory: %s", result));

        Observable<Data> disk = dataStore.count(Data.class).get().toSingle()
                .flatMapObservable(result -> {
                    Diary.d("Size of disk: %d", result);
                    if (result != 0) {
                        return dataStore.select(Data.class).get().toObservable();
                    }

                    return Observable.just(null);
                }).doOnNext(result -> Diary.d("From Disk: %s", result))
                .doOnNext(result -> {
                    if (result == null) {
                        Diary.w("Result is null!");
                    } else {
                        Diary.d("Version - %s :: Assets %s", result.version, result.getAssets());
                    }
                });

        Observable<Data> network = Observable.defer(() -> {
            Request request = new Request.Builder()
                    .url(URL)
                    .build();

            try {
                Response response = client.newCall(request)
                        .execute();

                data = mapper.readValue(response.body().byteStream(), Data.class);

                return Observable.just(data);
            } catch (Exception e) {
                return Observable.error(e);
            }
        })//.flatMap(result -> dataStore.upsert(result).toObservable())
                .doOnNext(result -> Diary.d("From Network: %s", result));

        return Observable.concat(memory, network)
                .first(data -> data != null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(data -> TranslateUtil.setLocale(data.locale, java.util.Locale.getDefault().toString()));
    }

    @Override
    public Observable<List<AssetGroupType>> getAssetGroupTypes() {
        return fetchData()
                .map(Data::getAssetGroupTypes);
    }

    @Override
    public Observable<List<AssetGroup>> getAssetGroups() {
        return fetchData()
                .map(Data::getAssetGroups);
    }

    @Override
    public Observable<AssetGroup> getAssetGroupById(String id) {
        return null;
    }

    @Override
    public Observable<List<Asset>> getAssets() {
        return fetchData()
                .map(Data::getAssets);
    }

    @Override
    public Observable<List<Asset>> getAssetsInGroup(String groupId) {
        return null;
    }

    @Override
    public Observable<Asset> getAssetById(String id) {
        return null;
    }

    @Override
    public Observable<Locale> getLocale() {
        //return fetchData()
               // .map(data -> data.locale);

        return null;
    }
}
