package com.lgvalle.beaufitulphotos.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgvalle on 04/05/14.
 * <p/>
 * Custom generic adapter.
 * This class aim to be as generic as possible to serve as the only adapter need in the app.
 * It works with a concrete {@link com.lgvalle.beaufitulphotos.utils.Renderer} to <i>render</i> a view for each row.
 * <p/>
 * This RendererAdapter works as an Adapter should work: just adapt one thing to another. No inflation or bind logic here
 */
public class RendererAdapter<Object> extends BaseAdapter {
	private final Context context;
	private final LayoutInflater layoutInflater;
	/* This class creates a view for each element of the adapter */
	private final Renderer renderer;
	/* List of generic elements */
	private List<Object> elements;

	public RendererAdapter(LayoutInflater layoutInflater, Renderer renderer, Context ctx) {
		this.layoutInflater = layoutInflater;
		this.elements = new ArrayList<Object>();
		this.renderer = renderer;
		this.context = ctx;
	}

	/**
	 * Adds elements to current elements list and refresh adapter
	 *
	 * @param moreElements List of elements to add
	 */
	public void addElements(List<Object> moreElements) {
		this.elements.addAll(moreElements);
		notifyDataSetChanged();
	}

	/**
	 * Clear current elements list and refresh adapter
	 */
	public void clear() {
		this.elements.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public Object getItem(int i) {
		return elements.get(i);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * For each element in the adapter, Android is going to call this method to <i>adapt</i> the object to the view.
	 * A render is used to build a view for that object.
	 * <p/>
	 * The renderer <b>r</b> is built from renderer concrete object. This handles view recycling.
	 * <p/>
	 * Once a renderer is built a call to <i>render</i> will create a view for current object
	 */
	@Override
	public final View getView(int position, View view, ViewGroup container) {
		Object element = getItem(position);
		Renderer r = renderer.build(element, layoutInflater, container, view);
		return r.render(context);
	}
}
