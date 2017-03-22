package com.prefanatic.wallpapersfromleagueoflegends;

import com.prefanatic.wallpapersfromleagueoflegends.data.AssetProvider;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAsset;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAssetGroup;
import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAssetGroupType;

import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import rx.functions.Action1;

import static org.junit.Assert.assertEquals;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    public AssetProvider provider;

    @Before
    public void init() {
        //provider = new AssetProviderImpl(new MockContext());
    }

    @Test
    public void canGetAssetGroupTypes() {
        provider.getAssetGroupTypes()
                .flatMapIterable(list -> list)
                .toBlocking()
                .subscribe(new PrintAction<AbstractAssetGroupType>());
    }

    @Test
    public void canGetAssetGroups() {
        provider.getAssetGroups()
                .flatMapIterable(list -> list)
                .toBlocking()
                .subscribe(new PrintAction<AbstractAssetGroup>());
    }

    @Test
    public void canGetAllAssets() {
        provider.getAssets()
                .flatMapIterable(list -> list)
                .toBlocking()
                .subscribe(new PrintAction<AbstractAsset>());
    }

    @Test
    public void canGetJaxAssets() {
        provider.getAssets()
                .flatMapIterable(list -> list)
                .filter(asset -> asset.id.contains("jax"))
                .subscribe(new PrintAction<AbstractAsset>());
    }

    @Test
    public void canGetTranslations() {
        /*rovider.getLocale()
                .map(locale -> locale.translations)
                .flatMapIterable(d -> d.values())
                .toBlocking()
                .subscribe(new PrintAction<Map<String, String>>());*/
    }

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    class TestObject {
        public int a;

        public TestObject(int a) {
            this.a = a;
        }
    }

    TestObject[] a = new TestObject[2];

    @Test
    public void testMatthewStuff() {
        a[0] = new TestObject(0);
        a[1] = new TestObject(1);

        doStuff(a[0], a[a[0].a]);

        assertEquals(a[0].a, 4);
    }

    public void doStuff(TestObject a, TestObject b) {
        a.a = 3;
        b.a = 4;
    }

    class PrintAction<T> implements Action1<T> {
        @Override
        public void call(T t) {
            System.out.println(t.toString());
        }
    }

    // Input
    String input = "10\n" +
            "Benjamin Christine Marjorie Florence Katie Cynthia Julia Deborah Stella Karen Roberta \n" +
            "Kenneth Julia Katie Roberta Florence Stella Cynthia Christine Marjorie Karen Deborah \n" +
            "Shane Karen Cynthia Katie Stella Christine Roberta Julia Marjorie Florence Deborah \n" +
            "Barry Stella Karen Julia Deborah Christine Cynthia Marjorie Roberta Katie Florence \n" +
            "Robert Florence Marjorie Deborah Roberta Karen Katie Christine Cynthia Stella Julia \n" +
            "David Julia Stella Florence Marjorie Cynthia Deborah Roberta Christine Katie Karen \n" +
            "Travis Deborah Roberta Katie Stella Julia Cynthia Florence Christine Marjorie Karen \n" +
            "Eddie Florence Katie Stella Karen Marjorie Deborah Julia Christine Roberta Cynthia \n" +
            "James Stella Florence Marjorie Katie Cynthia Julia Roberta Deborah Christine Karen \n" +
            "John Marjorie Deborah Julia Christine Stella Florence Roberta Karen Cynthia Katie \n" +
            "Roberta Robert Travis Shane Benjamin David John James Barry Eddie Kenneth \n" +
            "Christine Robert Benjamin Barry Shane Kenneth John James David Travis Eddie \n" +
            "Cynthia John Barry Eddie David Benjamin Robert Kenneth Shane James Travis \n" +
            "Julia Kenneth James Barry Travis John Eddie Benjamin David Robert Shane \n" +
            "Stella Benjamin Shane Kenneth James Barry Eddie Travis Robert John David \n" +
            "Karen Shane Barry Robert John Kenneth David James Travis Benjamin Eddie \n" +
            "Marjorie Barry John Travis Kenneth David Robert Eddie Benjamin James Shane \n" +
            "Florence John James Benjamin Eddie David Kenneth Travis Shane Barry Robert \n" +
            "Deborah Barry Travis David Eddie Robert John James Benjamin Shane Kenneth \n" +
            "Katie John Benjamin Eddie Robert Barry Travis James Kenneth Shane David ";

    Map<Integer, String> peopleMap = new HashMap<>();
    Map<String, Integer> reversePeopleMap = new HashMap<>(); // FIXME: 9/19/2016 What?
    Map<Integer, Integer[]> preferenceMap = new HashMap<>();
    Map<Integer, List<Integer>> proposalAttempts = new HashMap<>();
    List<Integer> freeMen = new ArrayList<>();

    Map<Integer, Integer> marriedPairs = new HashMap<>();

    int manIndex = 0;
    int manPreferenceIndex = 0;

    int runCount = 0;
    int MAX_RUN_COUNT = 5 * 10000;

    int numberOfGender;

    @Test
    public void amandaHomework() {
        String data = readInput("C:\\Users\\Cody Goldberg\\Desktop\\input.txt");
        log(data);
        parseInput(data);

        while (menAreFree()) {
            runCount++;
            if (runCount > MAX_RUN_COUNT) {
                log("FAILED.");
                break;
            }

            log("Index %d --> Max %d", manIndex, freeMen.size());

            // Get the man.
            Integer man = freeMen.get(manIndex);

            log("Man is %s", name(man));

            // If we've run out of preferences, give up.
            int maxPreferences = preferenceMap.get(man).length;
            if (manPreferenceIndex > maxPreferences) {
                manPreferenceIndex = 0;
                manIndex++;
            }

            // Who is his preference?
            Integer woman = preferenceMap.get(man)[manPreferenceIndex];

            // Lets not touch this woman again, if we've tried recently.
            List<Integer> recentlyProposed = proposalAttempts.get(man);
            if (!recentlyProposed.contains(woman)) {
                recentlyProposed.add(woman);
                proposalAttempts.put(man, recentlyProposed); // Is this needed?

                marryOrFightForMarrage(man, woman);
            } else {
                manPreferenceIndex++;
            }
        }

        // Log the output.
        log("Finished.");
        for (Map.Entry<Integer, Integer> marrage : marriedPairs.entrySet()) {
            log("%s -> %s", name(marrage.getValue()), name(marrage.getKey()));
        }
    }

    private void marryOrFightForMarrage(Integer man, Integer woman) {
        // Is the woman married?
        Integer womansTool = marriedPairs.get(woman);

        log("%s :: %s (%s)", name(man), name(woman), womansTool != null ? name(womansTool) : "unmarried");

        // If she isn't married...
        if (womansTool == null) {
            // He has become her tool!
            log("Marry as a result of no husband.");
            marry(woman, man);
        } else {
            // How does our current man face off in a battle between our current tool?
            int manPreference = findPreferenceById(woman, man);
            int toolPreference = findPreferenceById(woman, womansTool);

            log("%s :: Fight occurred between %s(%d) and %s(%d)", name(woman), name(man), manPreference, name(womansTool), toolPreference);

            // Is the new man better than her tool?
            if (manPreference < toolPreference) {
                // Good thing you can divorce!
                marry(woman, man);

                // She was already married, so put our poor man back in the pool of tools.
                freeMen.add(womansTool);
            }
        }
    }

    private void marry(Integer woman, Integer man) {
        marriedPairs.put(woman, man);

        log("Marrying %s to %s", name(woman), name(man));

        // Remove the man from free men... :[
        freeMen.remove(man);

        List<Integer> proposals = proposalAttempts.get(man);
        proposals.remove(woman);

        proposalAttempts.put(man, proposals);

        // Set our man index back to zero, so we might be able to get out.
        manIndex = 0;
        manPreferenceIndex = 0;
    }

    private int findPreferenceById(Integer subjectId, Integer preferenceId) {
        Integer[] preferences = preferenceMap.get(subjectId);
        for (int i = 0; i < preferences.length; i++) {
            if (preferences[i].equals(preferenceId)) {
                return i;
            }
        }

       return -1;
    }

    private boolean menAreFree() {
        return freeMen.size() > 0;
    }

    private void parseInput(String input) {
        // Split the input up into rows.
        String[] rows = input.trim().split("\n");

        // How many of each gender do we read?
        numberOfGender = Integer.valueOf(rows[0].trim());

        // Move down the rows and populate friends, with their preferences.
        for (int i = 1; i <= numberOfGender * 2; i++) {
            String person = rows[i].trim().split("\\s+")[0];
            log("Adding person %s :: %d", person, i);

            // Assuming males are listed first, add them as free men.
            if (i <= numberOfGender) {
                freeMen.add(i);
                proposalAttempts.put(i, new ArrayList<>());
            }

            peopleMap.put(i, person);
            reversePeopleMap.put(person, i);
        }

        // Perform a second pass to associate the preferences their owners.
        for (int i = 1; i <= numberOfGender * 2; i++) {
            String[] preferences = rows[i].trim().split("\\s+");
            String person = preferences[0];
            int personId = reversePeopleMap.get(person);
            Integer[] intPreferences = new Integer[preferences.length];

            log("%s (%d) has the following priorities:", person, personId);
            // For each member of their preference, add them to a map.
            for (int j = 1; j < preferences.length; j++) {
                intPreferences[j - 1] = reversePeopleMap.get(preferences[j]);
                log(" :: %s (%d)", preferences[j], intPreferences[j - 1]);
            }

            preferenceMap.put(personId, intPreferences);
        }
    }

    private String readInput(String path) {
        File file = new File(path);
/*
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] data = new byte[fileInputStream.available()];


            log(data);
            inputStream.close();

            return data;
        } catch (Exception e) {
            return "";
        }*/

        try {
            return new Scanner(file).useDelimiter("\\Z").next();
        } catch (Exception e) {
            return null;
        }
    }

    private String name(Integer id) {
        return peopleMap.get(id);
    }

    private void log(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }
}