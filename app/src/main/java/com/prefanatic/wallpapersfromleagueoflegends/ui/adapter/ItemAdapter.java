package com.prefanatic.wallpapersfromleagueoflegends.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created by cgoldberg01 on 10/19/16.
 */

public class ItemAdapter extends RecyclerView.Adapter {
    private final PublishSubject<ClickEvent> subject = PublishSubject.create();

    private SparseArray<Item> sparseItemArray;
    private List<Item> data;

    public ItemAdapter() {
        this.sparseItemArray = new SparseArray<>();
        this.data = new ArrayList<>();
    }

    public List<Item> getData() {
        return data;
    }

    /**
     * Observes ANY click event on items in the adapter.
     * The returned {@link ClickEvent} is generic.
     * <p>
     * This Observable automatically throttles events by 450 milliseconds, as a debouncer.
     *
     * @return Observable of ClickEvent
     */
    public Observable<ClickEvent> observeClicks() {
        return subject.asObservable()
                .throttleFirst(450, TimeUnit.MILLISECONDS);
    }

    /**
     * Observes click events on items in the adapter, given a filtering Class.
     * The returned {@link ClickEvent} is cast to the Class specified.
     * <p>
     * This Observable automatically throttles events by 450 milliseconds, as a debouncer.
     *
     * @param klass Class
     * @return Observable of ClickEvent
     */
    public <I extends Item> Observable<ClickEvent<I>> observeClicks(Class<I> klass) {
        return subject.asObservable()
                .filter(clickEvent -> clickEvent.data.getClass().equals(klass))
                .throttleFirst(450, TimeUnit.MILLISECONDS)
                .map(clickEvent -> clickEvent);
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();

        populateViewHolderArray();
    }

    // support single, collection
    // index, start, end, before, after
    public void insert() {

    }

    /**
     * Adds an item to the end of the list.
     *
     * @param item Item
     */
    public void insert(Item item) {
        data.add(item);
        notifyItemInserted(data.size());

        populateViewHolderArray();
    }

    /**
     * Adds a collection of items to the end of the list.
     *
     * @param items Items
     */
    public void insert(Collection<? extends Item> items) {
        int startSize = data.size();
        data.addAll(items);
        int endSize = data.size();

        notifyItemRangeInserted(startSize, endSize);
        populateViewHolderArray();
    }

    public void insertOrUpdate(Item item) {
        int index = data.indexOf(item);
        if (index == -1) {
            insert(item);
            return;
        }

        data.set(index, item);

        notifyItemChanged(index);
        populateViewHolderArray();
    }

    // single: item
    // collection: from, to
    public void replace() {

    }

    public void replace(Collection<? extends Item> items) {
        data.clear();
        data.addAll(items);
        notifyDataSetChanged();

        populateViewHolderArray();
    }

    public void replace(Collection<Item> items, int from, int to) {

    }

    // single: index, item
    // collection: from, to
    public void remove() {

    }

    /**
     * Loops through our data collection, and adds the apparent ViewHolders to the
     * sparseItemArray.
     */
    private void populateViewHolderArray() {
        for (Item item : data) {
            sparseItemArray.put(item.getLayout(), item);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        data.get(position).bindViewHolder(holder);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Item item = sparseItemArray.get(viewType);
        final RecyclerView.ViewHolder holder = item.getViewHolder(item.getItemView(parent.getContext(), parent));

        RxView.clicks(holder.itemView)
                .map(v -> {
                    // Its possible that the data might change between observing and mapping.
                    // Rare, but possible.  Just ignore that click!
                    if (holder.getAdapterPosition() >= data.size() || holder.getAdapterPosition() == -1) {
                        return null;
                    }

                    return new ClickEvent<>(holder, data.get(holder.getAdapterPosition()));
                })
                .filter(clickEvent -> clickEvent != null)
                //.doOnNext(clickEvent -> Diary.d("Clicked %s", clickEvent.viewHolder))
                .subscribe(subject);

        return holder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Use the layout as the view type.
        return data.get(position).getLayout();
    }

    public static final class ClickEvent<I extends Item> {
        public final RecyclerView.ViewHolder viewHolder;
        public final I data;

        public ClickEvent(RecyclerView.ViewHolder viewHolder, I data) {
            this.viewHolder = viewHolder;
            this.data = data;
        }
    }
}
