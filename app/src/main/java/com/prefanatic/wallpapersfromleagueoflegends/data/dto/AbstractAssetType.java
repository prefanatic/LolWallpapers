package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prefanatic.wallpapersfromleagueoflegends.util.TranslateUtil;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 3/30/2016)
 */
@JsonDeserialize(as = AssetType.class)
@Entity(stateless = true)
public abstract class AbstractAssetType implements Persistable {
    @Key public String id;
    public String nameTranslateId;

    @ManyToOne
    Data parent;

    public String getNameTranslated() {
        return TranslateUtil.translate(nameTranslateId);
    }
}
