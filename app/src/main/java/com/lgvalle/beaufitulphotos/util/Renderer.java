package com.lgvalle.beaufitulphotos.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lgvalle on 04/05/14.
 */
public abstract class Renderer<T> {

    protected View rootView;
    protected T content;


	/**
	 * Need public empty constructor to build empty renderer
	 */
    public Renderer() {}

    /**
     * Create or recycle a Renderer
     */
    public Renderer build(T content, LayoutInflater inflater, ViewGroup parent, View convertView) {
        Renderer r;
        if (convertView == null) {
            r = create(content, inflater, parent);
        } else {
            r = (Renderer) convertView.getTag();
            r.onRecycle(content);
        }

        return r;
    }

    /**
     * Default constructor
     */
    protected Renderer(T content, LayoutInflater inflater, ViewGroup parent) {
        this.content = content;
    }

    /**
     * When recycling a renderer, you only need to set a new content.
     */
    public void onRecycle(T content) {
        this.content = content;
    }

	protected T getContent() {
		return this.content;
	}

    public abstract View render(Context ctx);

    protected abstract <T> Renderer create(T content, LayoutInflater inflater, ViewGroup parent);



}
