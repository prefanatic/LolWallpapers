package com.prefanatic.wallpapersfromleagueoflegends;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.net.Uri;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.prefanatic.wallpapersfromleagueoflegends.util.Diary;

/**
 * com.prefanatic.wallpapersfromleagueoflegends (Cody Goldberg - 4/1/2016)
 */
public class WallpaperSlideshowService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new WallpaperEngine();
    }

    private class WallpaperEngine extends Engine {
        private WindowManager windowManager;

        private Bitmap bitmap;
        private MediaPlayer mediaPlayer;
        private boolean mediaPlayerIsPrepared = false;
        private int screenWidth, screenHeight;

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            attemptMediaPlayerRun();
            attemptPictureRun();
        }

        private void attemptMediaPlayerRun() {
            if (mediaPlayer == null) {
                // Do nothing - we're not using video.
                return;
            }

            if (getSurfaceHolder() == null || getSurfaceHolder().getSurface() == null) {
                Diary.w("Surface is null!");
                return;
            }

            if (!mediaPlayerIsPrepared) {
                Diary.w("MediaPlayer is not prepared!");
                return;
            }

            // Change the surface size
            int videoWidth = mediaPlayer.getVideoWidth();
            int videoHeight = mediaPlayer.getVideoHeight();
            int screenWidth = getDesiredMinimumWidth();

            int aspectHeight = (int) (((float) videoHeight / (float) videoWidth) * (float) screenWidth);
            getSurfaceHolder().setFixedSize(videoWidth, aspectHeight);
            getSurfaceHolder().setSizeFromLayout();

            mediaPlayer.setSurface(getSurfaceHolder().getSurface());
            mediaPlayer.start();
        }

        private void setAsVideo(String url) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(url));
            mediaPlayer.setLooping(true);
            mediaPlayer.setOnPreparedListener(mp -> {
                mediaPlayerIsPrepared = true;
                attemptMediaPlayerRun();
            });
        }

        private void attemptPictureRun() {
            if (getSurfaceHolder() == null || getSurfaceHolder().getSurface() == null) {
                Diary.w("Surface is null!");
                return;
            }

            if (bitmap == null) {
                Diary.w("Bitmap hasn't been downloaded yet!");
                return;
            }

            drawBitmap();
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            if (bitmap == null) return;

            translateCanvas(xPixelOffset);
        }

        private void drawBitmap() {
            Canvas canvas = getSurfaceHolder().lockCanvas();
            if (canvas != null) {
                canvas.drawBitmap(bitmap, 0, 0, null);
                getSurfaceHolder().unlockCanvasAndPost(canvas);
            }
        }

        private void translateCanvas(float dx) {
            Canvas canvas = getSurfaceHolder().lockCanvas();
            if (canvas != null) {
                canvas.translate(dx, 0);
            }
            getSurfaceHolder().unlockCanvasAndPost(canvas);
        }

        private void setAsPicture(String url) {
            Glide.with(getApplicationContext())
                    .load("http://screensaver.riotgames.com/latest/content/original/Champion%20Illustrations/Jax/Vandal_Jax_Splash.jpg")
                    .asBitmap()
                    .override(screenWidth, screenHeight)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            Diary.d("Screen (%dx%d) -- Bitmap (%dx%d)", screenWidth, screenHeight, resource.getWidth(), resource.getHeight());

                            // Calculate aspect ratio.
                            // (oH/oW) x nW = nH
                            // (oH/oW) = (nH/nW)
                            // nH / aspect = nW

                            float aspect = (float) resource.getHeight() / resource.getWidth();
                            int width = (int) (screenHeight / aspect);

                            bitmap = Bitmap.createScaledBitmap(resource, width, screenHeight, false);
                            attemptPictureRun();
                        }
                    });
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

            Point point = new Point();
            windowManager.getDefaultDisplay().getSize(point);
            screenWidth = point.x;
            screenHeight = point.y;

            // setAsVideo("http://screensaver.riotgames.com/latest/content/original/Animated%20Art/aetherwing-kayle-loop.webm");

            /**/

            setAsPicture("");

        }
    }
}
