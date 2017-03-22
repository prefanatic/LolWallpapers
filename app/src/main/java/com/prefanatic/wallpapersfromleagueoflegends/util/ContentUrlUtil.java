package com.prefanatic.wallpapersfromleagueoflegends.util;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.util (Cody Goldberg - 3/30/2016)
 */
public class ContentUrlUtil {
    private static final String PREVIEW = "http://screensaver.riotgames.com/latest/content/original/%s";
    private static final String THUMBNAIL = "http://screensaver.riotgames.com/latest/content/%s";


    public static String thumbnail(String loc) {
        return String.format(THUMBNAIL, loc);
    }

    public static String preview(String loc) {
        return String.format(PREVIEW, loc);
    }
}
