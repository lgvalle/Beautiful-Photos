package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.InjectView;
import butterknife.OnClick;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BlurTransform;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by lgvalle on 22/07/14.
 * <p/>
 * Display a single photo in full screen.
 * <p/>
 * Initializes photoview with already cached thumbnail while fetching large image
 */
public class DetailsFragment extends BaseFragment {
	private static final String TAG = DetailsFragment.class.getSimpleName();
	private static final String EXTRA_PHOTO = "extra_photo";
	private PhotoModel photo;
	private boolean isFullscreen;

	@InjectView(R.id.photo)
	ImageView ivPhoto;
	@InjectView(R.id.photo_thumbnail)
	ImageView ivPhotoThumbnail;
	@InjectView(R.id.photo_author)
	TextView tvPhotoAuthor;
	@InjectView(R.id.info_container)
	View vInfoContainer;


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
		// Restore actionbar app title
		setActionBarTitle(getString(R.string.app_name));
		// Restore UI in any case
		showSystemUI();
	}

	public static DetailsFragment newInstance(PhotoModel photo) {
		DetailsFragment f = new DetailsFragment();
		Bundle args = new Bundle();
		args.putParcelable(EXTRA_PHOTO, photo);
		f.setArguments(args);
		return f;
	}

	/**
	 * Click on photo toggle fullscreen visibility
	 */
	@OnClick(R.id.photo)
	public void onClickPhoto() {
		if (isFullscreen) {
			showSystemUI();
		} else {
			hideSystemUI();
		}
		isFullscreen = !isFullscreen;
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photo_details;
	}

	/**
	 * Get Photo from arguments and init layout
	 */
	@Override
	protected void initLayout() {
		photo = getArguments().getParcelable(EXTRA_PHOTO);

		// Text fields: author and photo title
		tvPhotoAuthor.setText(photo.getAuthorName());

		// Also, set photo title on actionbar
		setActionBarTitle(photo.getTitle());

		// Load cached placeholder image into ivPhotoThumbnail image view. This should be instant from cache
		// After that, always load large photo
		Picasso.with(getActivity()).load(photo.getSmallUrl()).transform(new BlurTransform(getActivity())).into(ivPhotoThumbnail, new Callback() {
			@Override
			public void onError() {
				loadLargePhoto();
			}

			@Override
			public void onSuccess() {
				loadLargePhoto();
			}
		});


	}

	/**
	 * Hide action bar and info container. Dim UI
	 */
	private void hideSystemUI() {
		getActivity().getActionBar().hide();
		getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		vInfoContainer.setVisibility(View.GONE);
	}

	/**
	 * Load full size image
	 */
	private void loadLargePhoto() {
		// This could be called with some delay, so check if activity still exists.
		if (getActivity() != null) {
			Picasso.with(getActivity()).load(photo.getLargeUrl()).into(ivPhoto, new Callback() {
				@Override
				public void onSuccess() {
					// After large photo is loaded, hide thumbnail imageview
					ivPhotoThumbnail.setVisibility(View.GONE);
				}

				@Override
				public void onError() {
					Toast.makeText(getActivity(), getString(R.string.service_error), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * Restore UI original visibility
	 */
	private void showSystemUI() {
		getActivity().getActionBar().show();
		// Clear all flags
		getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
		vInfoContainer.setVisibility(View.VISIBLE);
	}
}
