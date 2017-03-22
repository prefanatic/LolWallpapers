package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

import io.requery.Entity;
import io.requery.ForeignKey;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.OneToOne;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 3/30/2016)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = Data.class)
@Entity(stateless = true)
public abstract class AbstractData {
    @Key public String version;
    public String versionDate;

    @OneToMany
    public List<AssetGroupType> assetGroupTypes;

    @OneToMany
    public List<AssetType> assetTypes;

    @OneToMany
    public List<AssetGroup> assetGroups;

    @OneToMany
    public List<Asset> assets;

    @ForeignKey
    @OneToOne
    public Locale locale;
}
