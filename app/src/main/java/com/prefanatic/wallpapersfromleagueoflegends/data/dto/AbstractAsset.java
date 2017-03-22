package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.util.List;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 3/30/2016)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = Asset.class)
@Entity(stateless = true)
public class AbstractAsset implements Persistable {
    @Key public String id;
    public String url;
    public List<String> tags;
    public String type;
    public long size;
    public String thumbnailUrl;
    public long thumbnailSize;
    public String thumbnailVideoUrl;
    public long thumbnailVideoSize;
   // public Date dateAdded;
    public String introUrl;
    public int loopStartTime;

    @ManyToOne
    Data parent;
}
