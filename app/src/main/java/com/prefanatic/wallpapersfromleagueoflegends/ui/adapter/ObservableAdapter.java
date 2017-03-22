package com.prefanatic.wallpapersfromleagueoflegends.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;

/**
 * com.prefanatic.wallpapersfromleagueoflegends.ui.adapter (Cody Goldberg - 3/31/2016)
 */
public abstract class ObservableAdapter<D, VH extends ObservableAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {
    private final List<D> data = new ArrayList<>();
    private final SerializedSubject<D, D> subject = new SerializedSubject<>(PublishSubject.create());

    public void add(D item) {
        data.add(item);

        notifyItemChanged(data.size());
    }

    public Observable<D> getClickObservable() {
        return subject.asObservable();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, data.get(position));
    }

    abstract void onBindViewHolder(VH holder, D item);

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
            RxView.clicks(itemView)
                    .map(n -> data.get(getAdapterPosition()))
                    .subscribe(subject::onNext);
        }
    }
}
