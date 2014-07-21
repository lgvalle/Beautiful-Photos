package com.lgvalle.photosnearby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.lgvalle.photosnearby.util.RowRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgvalle on 04/05/14.
 */
public class RowRendererAdapter<Object> extends BaseAdapter {
	private final LayoutInflater layoutInflater;
    private final RowRenderer renderer;
	private final Context context;
	private List<Object> elements;

    public RowRendererAdapter(LayoutInflater layoutInflater, RowRenderer renderer, Context ctx) {
        this.layoutInflater = layoutInflater;
        this.elements = new ArrayList<Object>();
        this.renderer = renderer;
	    this.context = ctx;
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

    @Override
    public final View getView(int position, View view, ViewGroup container) {
	    Object element = getItem(position);
        RowRenderer r = renderer.build(element, layoutInflater, container, view);
        return r.render(context);
    }

    public void setElements(List<Object> elements) {
        this.elements = elements;
	    notifyDataSetChanged();
    }

	public List<Object> getElements() {
		return this.elements;
	}

	public void addElements(List<Object> moreElements){
		this.elements.addAll(moreElements);
	}
}
