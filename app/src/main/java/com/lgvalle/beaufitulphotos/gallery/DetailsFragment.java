package com.lgvalle.beaufitulphotos.gallery;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.InjectView;
import butterknife.OnClick;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Display a single photo in full screen.
 * <p/>
 * Initializes photoview with already cached thumbnail while fetching large image
 */
public class DetailsFragment extends BaseFragment {
	private static final String EXTRA_PHOTO = "extra_photo";
	@InjectView(R.id.photo)
	ImageView ivPhoto;
	@InjectView(R.id.photo_title)
	TextView tvPhotoTitle;
	@InjectView(R.id.photo_author)
	TextView tvPhotoAuthor;
	private PhotoModel photo;

	@Override
	public void onResume() {
		super.onResume();
		// Show back button in actionbar
		displayHomeAsUp(true);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Remove back button when exit
		displayHomeAsUp(false);
	}

	public static DetailsFragment newInstance(PhotoModel photo) {
		DetailsFragment f = new DetailsFragment();
		Bundle args = new Bundle();
		args.putSerializable(EXTRA_PHOTO, photo);
		f.setArguments(args);
		return f;
	}

	@OnClick(R.id.photo)
	public void onClickLayout() {
		/*
		if (isFullscreen) {
			showSystemUI();
			isFullscreen = false;
		} else {
			hideSystemUI();
			isFullscreen = true;
		}
		*/
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photo_details;
	}

	@Override
	protected void initLayout() {
		photo = (PhotoModel) getArguments().getSerializable(EXTRA_PHOTO);

		// Text fields: author and photo title
		tvPhotoTitle.setText(photo.getTitle());
		tvPhotoAuthor.setText(photo.getAuthorName());

		// Load cached placeholder image into ivPhoto image view. This should be instant from cache
		Picasso.with(getActivity())
				.load(photo.getSmallUrl())
				.into(new Target() {
					@Override
					public void onBitmapFailed(Drawable errorDrawable) {
						loadLargePhoto();
					}

					@Override
					public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
						ivPhoto.setImageBitmap(bitmap);
						loadLargePhoto();
					}

					@Override
					public void onPrepareLoad(Drawable placeHolderDrawable) { /*nothing to do*/ }
				});

	}

	private void hideSystemUI() {
		getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
				| View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
		);
	}

	private void loadLargePhoto() {
		// This could be called with some delay, so check if activity still exists.
		if (getActivity() != null) {
			Picasso.with(getActivity())
					.load(photo.getLargeUrl())
					.into(ivPhoto);
		}

	}

	private void showSystemUI() {
		getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
	}
}
