package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends (Cody Goldberg - 5/21/2016)
 */
@Entity
public abstract class AbstractTest {
    public String string;

    @Key public int integer;

    @ManyToOne
    public InterfaceTestEntity test;
}
