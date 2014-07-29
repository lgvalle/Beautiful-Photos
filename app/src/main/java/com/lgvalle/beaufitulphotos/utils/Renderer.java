package com.lgvalle.beaufitulphotos.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lgvalle on 04/05/14.
 * <p/>
 * This generic abstract class has two main responsibilities:
 * <li>Handle view recycling</li>
 * <li>Bind view elements with model and generate a view for them. This should be done by child classes. So is an abstract method</li>
 */
public abstract class Renderer<T> {
	protected View rootView;
	protected T content;

	/**
	 * Need public empty constructor to build empty renderer
	 */
	public Renderer() {}

	/**
	 * Default constructor
	 */
	protected Renderer(T content) {
		this.content = content;
	}

	/**
	 * This method is called by RendererAdapter.
	 * <p/>
	 * Child classes need to implement this method to bind view with model and provide a view object for them.
	 *
	 * @return A view representing <b>this.content</b>
	 */
	public abstract View render(Context ctx);

	/**
	 * Child classes need to implement this method to provide a concrete renderer for each model object
	 *
	 * @param content Model object for which concrete renderer is providing a view
	 */
	protected abstract <T> Renderer create(T content, LayoutInflater inflater, ViewGroup parent);

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
	 * When recycling a renderer, you only need to set a new content.
	 */
	protected void onRecycle(T content) {
		this.content = content;
	}

	/**
	 * @return Model object of this renderer
	 */
	protected T getContent() {
		return this.content;
	}


}
