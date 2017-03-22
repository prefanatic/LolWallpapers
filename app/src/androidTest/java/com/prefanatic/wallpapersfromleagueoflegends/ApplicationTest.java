package com.prefanatic.wallpapersfromleagueoflegends;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.prefanatic.wallpapersfromleagueoflegends.data.dto.InterfaceTest;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.InterfaceTestEntity;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.Models;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.WtfInterface;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.WtfInterfaceEntity;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.android.sqlite.DatabaseSource;
import io.requery.sql.EntityDataStore;
import io.requery.sql.TableCreationMode;

import static org.junit.Assert.assertEquals;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
@RunWith(AndroidJUnit4.class)
public class ApplicationTest {
    public static EntityDataStore<Persistable> dataStore;

    @BeforeClass
    public static void init() {
        DatabaseSource source = new DatabaseSource(InstrumentationRegistry.getTargetContext(), Models.DEFAULT, 1);
        source.setTableCreationMode(TableCreationMode.DROP_CREATE);

        dataStore = new EntityDataStore<>(source.getConfiguration());
    }

    @Test
    public void testInterfaceModel() {
        dataStore.delete(InterfaceTestEntity.class).get().value();

        InterfaceTestEntity entity = new InterfaceTestEntity();
        entity.string("test");

        for (int i = 0; i < 10; i++) {
            WtfInterfaceEntity test = new WtfInterfaceEntity();
            test.setId(i);
            test.entity(entity);

            entity.getTests().add(test);
        }

        dataStore.insert(entity);
        int count = dataStore.count(InterfaceTest.class).get().value();

        assertEquals(1, count);
    }
}