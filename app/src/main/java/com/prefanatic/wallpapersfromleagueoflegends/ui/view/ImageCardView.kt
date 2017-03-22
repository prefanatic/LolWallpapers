package com.prefanatic.wallpapersfromleagueoflegends.ui.view

import android.R.attr.gravity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.ImageViewTarget
import com.prefanatic.wallpapersfromleagueoflegends.util.Diary
import com.prefanatic.wallpapersfromleagueoflegends.util.PaletteBitmap
import com.prefanatic.wallpapersfromleagueoflegends.util.PaletteBitmapTranscoder

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui.view (Cody Goldberg - 3/30/2016)
 */
class ImageCardView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null,
                                              defStyle: Int = 0)
    : FrameLayout(context, attributeSet, defStyle) {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView

    val SPACING_NORMAL = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics).toInt()
    val SPACING_SMALL = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()

    init {
        init()
    }

    private fun init() {
        imageView = ImageView(context)
        textView = TextView(context)

        val imageParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val textParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        textParams.apply {
            gravity = Gravity.BOTTOM or Gravity.START
        }

        textView.setPadding(SPACING_NORMAL, SPACING_SMALL, SPACING_NORMAL, SPACING_SMALL)

        imageView.adjustViewBounds = true

        addView(imageView, imageParams)
        addView(textView, textParams)

        layoutParams = ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    fun setImageUrl(url: String) {
        if (measuredWidth != 0) {
            minimumHeight = (measuredWidth / 0.7f).toInt()
        }

        Glide.with(context)
                .load(url)
                .asBitmap()
                .transcode(PaletteBitmapTranscoder(context), PaletteBitmap::class.java)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : ImageViewTarget<PaletteBitmap>(imageView) {
                    override fun setResource(resource: PaletteBitmap?) {
                        super.view.setImageBitmap(resource!!.bitmap)

                        textView.setBackgroundColor(resource.palette.getVibrantColor(Color.TRANSPARENT))
                        textView.setTextColor(resource.palette.vibrantSwatch?.titleTextColor ?: Color.BLACK)
                    }
                })
    }

    fun setTitle(title: String) {
        textView.text = title
    }
}
