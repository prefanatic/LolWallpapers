package com.prefanatic.wallpapersfromleagueoflegends.data.dto;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.prefanatic.wallpapersfromleagueoflegends.R;
import com.prefanatic.wallpapersfromleagueoflegends.ui.adapter.Item;
import com.prefanatic.wallpapersfromleagueoflegends.ui.view.ImageCardView;
import com.prefanatic.wallpapersfromleagueoflegends.util.ContentUrlUtil;
import com.prefanatic.wallpapersfromleagueoflegends.util.TranslateUtil;

import java.util.List;

import io.requery.Entity;
import io.requery.Key;
import io.requery.ManyToOne;
import io.requery.Persistable;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.data.dto (Cody Goldberg - 3/30/2016)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(as = AssetGroup.class)
@Entity(stateless = true)
public abstract class AbstractAssetGroup implements Persistable, Item<AbstractAssetGroup.ViewHolder> {
    @Key public String id;
    public String previewUrl;
    public List<String> assets;
    public List<String> tags;
    public String nameTranslateId;
    public String previewThumbnailUrl;
    public long previewThumbnailSize;

    @ManyToOne
    Data parent;

    public String getNameTranslated() {
        return TranslateUtil.translate(nameTranslateId);
    }

    @Override
    public void bindViewHolder(ViewHolder holder) {
        ImageCardView card = ((ImageCardView) holder.itemView);

        card.setImageUrl(ContentUrlUtil.thumbnail(previewThumbnailUrl));
        card.setTitle(getNameTranslated());
    }

    @Override
    public int getLayout() {
        return 0;
    }

    @Override
    public View getItemView(Context context, ViewGroup parent) {
        return new ImageCardView(context);
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        return new ViewHolder(view);
    }

    static final class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
