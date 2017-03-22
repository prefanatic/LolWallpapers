package com.prefanatic.wallpapersfromleagueoflegends.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Interface used for the ItemAdapter.
 * Objects in the ItemAdapter must inherit this Item interface.
 */

public interface Item<VH extends RecyclerView.ViewHolder> {
    /**
     * Returns a layout resource.  This also doubles as the Adapter's ViewType.
     *
     * @return
     */
    @LayoutRes
    int getLayout();

    /**
     * Returns a ViewHolder.
     *
     * @param view View (from {@link #getItemView(Context, ViewGroup)}
     * @return {@link VH}
     */
    VH getViewHolder(View view);

    /**
     * Returns a View to populate the ViewHolder's itemView with.
     *
     * @param context Context
     * @param parent  ViewGroup Parent
     * @return ItemView
     */
    View getItemView(Context context, ViewGroup parent);

    /**
     * Called when the ItemAdapter binds a data set with the VH specified.
     *
     * @param holder VH
     */
    void bindViewHolder(VH holder);
}
