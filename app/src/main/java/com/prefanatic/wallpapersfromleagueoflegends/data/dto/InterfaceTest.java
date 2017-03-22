package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import java.util.List;

import io.requery.CascadeAction;
import io.requery.Entity;
import io.requery.Generated;
import io.requery.Key;
import io.requery.OneToMany;
import io.requery.Persistable;
import io.requery.query.MutableResult;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 5/21/2016)
 */
@Entity
public interface InterfaceTest extends Persistable {
    @Key @Generated
    int id();

    @OneToMany
    List<WtfInterfaceEntity> getTests();

    String string();

}
