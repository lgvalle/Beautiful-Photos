package com.lgvalle.beaufitulphotos.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.util.Renderer;
import com.squareup.picasso.Picasso;

/**
 * Created by lgvalle on 21/07/14.
 */
public class GalleryItemRenderer extends Renderer<PhotoModel> {
	@InjectView(R.id.photo_title)
	TextView tvPhotoTitle;
	@InjectView(R.id.photo)
	ImageView ivPhoto;

	public GalleryItemRenderer() {
		super();
	}

	private GalleryItemRenderer(PhotoModel quest, LayoutInflater inflater, ViewGroup parent) {
		super(quest, inflater, parent);
		this.rootView = inflater.inflate(R.layout.row_photo, parent, false);
		this.rootView.setTag(this);
		ButterKnife.inject(this, rootView);
	}

	@Override
	public View render(Context ctx) {
		tvPhotoTitle.setText(getContent().getTitle());
		Picasso.with(ctx).load(getContent().getSmallUrl()).into(ivPhoto);
		return rootView;
	}

	@Override
	protected <T> Renderer create(T content, LayoutInflater inflater, ViewGroup parent) {
		return new GalleryItemRenderer((PhotoModel) content, inflater, parent);
	}
}
