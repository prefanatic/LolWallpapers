package com.prefanatic.wallpapersfromleagueoflegends;

import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

/**
 * com.prefanatic.wallpapersfromleagueoflegends (Cody Goldberg - 7/31/2016)
 */
public class MuzeiProviderService extends RemoteMuzeiArtSource {
    private static final String SOURCE_NAME = "LeagueWallpapers";
    private static final Uri jaxUri = Uri.parse("http://screensaver.riotgames.com/latest/content/original/Champion%20Illustrations/Jax/Vandal_Jax_Splash.jpg");

    public MuzeiProviderService() {
        super(SOURCE_NAME);
    }

    public MuzeiProviderService(String name) {
        super(name);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        Artwork artwork = new Artwork.Builder()
                .title("Test")
                .byline("Test")
                .imageUri(jaxUri)
                .token("jaxToken")
                .build();
        publishArtwork(artwork);
    }
}
