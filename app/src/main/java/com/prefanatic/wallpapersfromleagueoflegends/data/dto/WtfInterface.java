package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 5/21/2016)
 */
@Entity
public interface WtfInterface extends Persistable {
    @Key int getId();

    @ManyToOne
    InterfaceTestEntity entity();
}
