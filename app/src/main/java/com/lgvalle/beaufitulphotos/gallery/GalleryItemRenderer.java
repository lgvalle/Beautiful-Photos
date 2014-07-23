package com.lgvalle.beaufitulphotos.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.Renderer;
import com.squareup.picasso.Picasso;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * Concrete renderer for photomodel object.
 * <p/>
 * This class binds a concrete view with a concrete object.
 */
public class GalleryItemRenderer extends Renderer<PhotoModel> {
	@InjectView(R.id.photo)
	ImageView ivPhoto;

	public GalleryItemRenderer() {
		super();
	}

	private GalleryItemRenderer(PhotoModel quest, LayoutInflater inflater, ViewGroup parent) {
		super(quest);
		this.rootView = inflater.inflate(R.layout.row_photo, parent, false);
		this.rootView.setTag(this);
		ButterKnife.inject(this, rootView);
	}

	/**
	 * Generate a view for this renderer content.
	 *
	 * @param ctx Context
	 * @return A view representing current content
	 */
	@Override
	public View render(Context ctx) {
		// In this example, generated view is just an image view
		Picasso.with(ctx).load(getContent().getSmallUrl()).into(ivPhoto);
		return rootView;
	}

	/**
	 * Creates a concrete implementation of a renderer
	 *
	 * @param content Model object for which concrete renderer is providing a view
	 */
	@Override
	protected <T> Renderer create(T content, LayoutInflater inflater, ViewGroup parent) {
		return new GalleryItemRenderer((PhotoModel) content, inflater, parent);
	}
}
