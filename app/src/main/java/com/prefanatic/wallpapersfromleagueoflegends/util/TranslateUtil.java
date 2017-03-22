package com.prefanatic.wallpapersfromleagueoflegends.util;

import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractLocale;

import java.util.Map;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.util (Cody Goldberg - 4/1/2016)
 */
public class TranslateUtil {
    private static Map<String, String> translations;

    public static void setLocale(AbstractLocale localeData, String locale) {
        translations = localeData.translations.get(locale);

        if (translations == null) {
            translations = localeData.translations.get("en_US");
        }
    }

    public static String translate(String key) {
        String translation = translations.get(key);
        if (translation == null) translation = key;

        return translation;
    }
}
