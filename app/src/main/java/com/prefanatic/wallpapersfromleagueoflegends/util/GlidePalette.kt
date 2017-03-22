package com.prefanatic.wallpapersfromleagueoflegends.util

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.bumptech.glide.util.Util

/**
 * com.prefanatic.wallpapersfromleagueoflegends.util (Cody Goldberg - 11/20/2016)
 */

data class PaletteBitmap(
        val bitmap: Bitmap,
        val palette: Palette
)

class PaletteBitmapResource(val paletteBitmap: PaletteBitmap, val bitmapPool: BitmapPool) : Resource<PaletteBitmap> {
    override fun get() = paletteBitmap

    override fun getSize() = Util.getBitmapByteSize(paletteBitmap.bitmap)

    override fun recycle() {
        if (!bitmapPool.put(paletteBitmap.bitmap)) {
            paletteBitmap.bitmap.recycle()
        }
    }
}

class PaletteBitmapTranscoder(val context: Context) : ResourceTranscoder<Bitmap, PaletteBitmap> {
    val bitmapPool = Glide.get(context).bitmapPool!!

    override fun transcode(toTranscode: Resource<Bitmap>?): Resource<PaletteBitmap> {
        val bitmap = toTranscode!!.get()
        val palette = Palette.Builder(bitmap).generate()
        val result = PaletteBitmap(bitmap, palette)

        return PaletteBitmapResource(result, bitmapPool)
    }

    override fun getId(): String = PaletteBitmapTranscoder::class.java.name
}