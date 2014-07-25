package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.rebound.*;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BlurTransformation;
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
	private final BaseSpringSystem mSpringSystem = SpringSystem.create();
	private final ExampleSpringListener mSpringListener = new ExampleSpringListener();
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
	private Spring mScaleSpring;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mScaleSpring = mSpringSystem.createSpring();
	}

	@Override
	public void onResume() {
		super.onResume();
		// Show back button in actionbar
		displayHomeAsUp(true);
		mScaleSpring.addListener(mSpringListener);
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

		mScaleSpring.removeListener(mSpringListener);
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
	//@OnClick(R.id.photo)
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(getContentView(), container, false);
		// Inject views after inflating and just before init layout. This way every child fragment follows correct sequence
		ButterKnife.inject(this, rootView);
		initLayout();
		ivPhoto.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Log.d(TAG, "[DetailsFragment - onTouch] - (line 108): " + "touch");
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						mScaleSpring.setEndValue(1);
						break;
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						mScaleSpring.setEndValue(0);
						break;
				}
				return true;
			}
		});
		return rootView;
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
		Picasso.with(getActivity()).load(photo.getSmallUrl()).transform(new BlurTransformation(getActivity())).into(ivPhotoThumbnail, new Callback() {
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

	private class ExampleSpringListener extends SimpleSpringListener {
		@Override
		public void onSpringUpdate(Spring spring) {
			float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 2);
			ivPhoto.setScaleX(mappedValue);
			ivPhoto.setScaleY(mappedValue);
		}
	}
}
