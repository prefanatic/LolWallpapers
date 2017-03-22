package com.prefanatic.wallpapersfromleagueoflegends.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.prefanatic.wallpapersfromleagueoflegends.data.dto.AbstractAsset;
import com.prefanatic.wallpapersfromleagueoflegends.ui.view.ImageCardView;
import com.prefanatic.wallpapersfromleagueoflegends.util.ContentUrlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui.adapter (Cody Goldberg - 3/31/2016)
 */
public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.ViewHolder> {
    private final List<AbstractAsset> data = new ArrayList<>();

    public void add(AbstractAsset group) {
        data.add(group);

        notifyItemInserted(data.size());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AbstractAsset asset = data.get(position);
        ImageCardView card = ((ImageCardView) holder.itemView);

        card.setImageUrl(ContentUrlUtil.thumbnail(asset.thumbnailUrl));
        card.setTitle(asset.id);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(new ImageCardView(parent.getContext()));
        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return holder;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
