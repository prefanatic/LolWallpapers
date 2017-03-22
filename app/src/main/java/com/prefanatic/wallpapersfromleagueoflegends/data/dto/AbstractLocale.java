package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

import io.requery.Entity;
import io.requery.OneToOne;
import io.requery.Persistable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 4/1/2016)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class AbstractLocale implements Persistable {
    public String stuff;

    public Map<String, Map<String, String>> translations;
}
