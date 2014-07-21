package com.lgvalle.photosnearby.gallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lgvalle.photosnearby.R;
import com.lgvalle.photosnearby.model.Photo500Px;
import com.lgvalle.photosnearby.util.Renderer;
import com.squareup.picasso.Picasso;

/**
 * Created by lgvalle on 21/07/14.
 */
public class PhotosRenderer extends Renderer<Photo500Px> {

	@InjectView(R.id.photo_title)
	TextView tvPhotoTitle;

	@InjectView(R.id.photo)
	ImageView ivPhoto;

	public PhotosRenderer() {
		super();
	}

	private PhotosRenderer(Photo500Px quest, LayoutInflater inflater, ViewGroup parent) {
		super(quest, inflater, parent);
		this.rootView = inflater.inflate(R.layout.row_photo, parent, false);
		this.rootView.setTag(this);

		ButterKnife.inject(this, rootView);
	}

	@Override
	public View render(Context ctx) {
		tvPhotoTitle.setText(getContent().getName());
		Picasso.with(ctx).load(getContent().getImageUrl()).into(ivPhoto);
		return rootView;
	}

	@Override
	protected <T> Renderer create(T content, LayoutInflater inflater, ViewGroup parent) {
		return new PhotosRenderer((Photo500Px) content, inflater, parent);
	}
}
