package com.lgvalle.photosnearby.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lgvalle on 04/05/14.
 */
public abstract class RowRenderer<T> {

    protected View rootView;
    protected T content;


    public RowRenderer() {}

    /**
     * Create or recycle a Renderer
     */
    public RowRenderer build(T content, LayoutInflater inflater, ViewGroup parent, View convertView) {
        RowRenderer r;
        if (convertView == null) {
            r = create(content, inflater, parent);
        } else {
            r = (RowRenderer) convertView.getTag();
            r.onRecycle(content);
        }

        return r;
    }

    /**
     * Default constructor
     */
    protected RowRenderer(T content, LayoutInflater inflater, ViewGroup parent) {
        this.content = content;
    }

    /**
     * When recycling a renderer, you only need to set a new content.
     */
    public void onRecycle(T content) {
        this.content = content;
    }

    public abstract View render(Context ctx);

    protected abstract <T> RowRenderer create(T content, LayoutInflater inflater, ViewGroup parent);



}
