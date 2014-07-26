package com.lgvalle.beaufitulphotos;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.facebook.rebound.*;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiModule500px;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Feature;
import com.lgvalle.beaufitulphotos.gallery.DetailsFragment;
import com.lgvalle.beaufitulphotos.gallery.GalleryFragment;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.otto.Subscribe;


/**
 * Main activity
 * <p/>
 * This class is on UI layer, so it's only responsible for UI interactions.
 * <p/>
 * It loads a Presenter to manage all business logic: data fetching and caching.
 * <p/>
 * The UI consist in two fragments: one with a list of photos and one for photo details.
 * <p/>
 * Finally, the activity (screen) creates a presenter and ask for photos. Results communication will happen through the event bus
 */
public class BeautifulPhotosActivity extends BaseActivity implements BeautifulPhotosScreen {
	private static final String TAG = BeautifulPhotosActivity.class.getSimpleName();
	public static final String FRAGM2ENT_GALLERY_TAG = "fragment_gallery_tag";
	/* Manage all business logic for this activity */
	private BeautifulPhotosPresenter presenter;
	/* Flag to control toggle between popular and highest rated feeds */
	private boolean popular;

	@InjectView(R.id.frame_details_content)
	View frameDetailsContent;

	@InjectView(R.id.main_content)
	FrameLayout mainContent;

	@InjectView(R.id.sliding_layout)
	SlidingUpPanelLayout slidingPanel;

	/** Spring animations */
	private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 10);
	private Spring mSpring;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mSpring = SpringSystem
				.create()
				.createSpring()
				.setSpringConfig(ORIGAMI_SPRING_CONFIG)
				.addListener(new SimpleSpringListener() {
					@Override
					public void onSpringUpdate(Spring spring) {
						// Just tell the UI to update based on the springs current state.
						//render();
					}
				});


	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Register on bus to let activity and presenter listen to events
		BusHelper.register(this);
		BusHelper.register(presenter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister every time activity is paused
		BusHelper.unregister(this);
		BusHelper.unregister(presenter);
	}

	/**
	 * Listen to gallery item selection
	 *
	 * @param event Event containing selected item
	 */
	@Subscribe
	public void onGalleryItemChosen(GalleryItemChosenEvent event) {
		if (event != null && event.getPhoto() != null) {
			// Instance details fragment with photo item
			DetailsFragment details = DetailsFragment.newInstance(event.getPhoto());
			// Loading target depends on device size: tablet or handset


			replaceFragment(R.id.frame_details_content, details);

			Log.d(TAG, "[BeautifulPhotosActivity - onGalleryItemChosen] - (line 95): " + "click");


			slidingPanel.expandPanel();
			//mSpring.setEndValue(1);

		}
	}

	@Override
	public void showError(int errorID) {
		Toast.makeText(this, getString(errorID), Toast.LENGTH_SHORT).show();
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initActionBar() {
		super.initActionBar();
		getActionBar().setDisplayShowTitleEnabled(false);
	}

	@Override
	protected void initLayout() {
		ButterKnife.inject(this);
		// Add Gallery Fragment to main_content frame. If this is a tablet there will be another frame to add content
		GalleryFragment galleryFragment = GalleryFragment.newInstance();
		addFragment(R.id.main_content, galleryFragment, FRAGM2ENT_GALLERY_TAG);

		frameDetailsContent.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						//render();
						frameDetailsContent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
					}
				});

	}

	@Override
	protected void initPresenter() {
		// Init activity presenter with all it's dependencies
		presenter = new BeautifulPhotosPresenterImpl(this, ApiModule500px.getService());
		// Request data (photos) to activity presenter. Answer will be post on bus, so no need to callbacks here
		presenter.needPhotos(Feature.HighestRated.getParam());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	private void render() {
		double value = mSpring.getCurrentValue();
		// Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
		if (frameDetailsContent != null) {
			Log.d(TAG, "[BeautifulPhotosActivity - render] - (line 153): " + "animation!");
			float barPosition = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, frameDetailsContent.getHeight(), 0);

			frameDetailsContent.setTranslationY(barPosition);
			//frameDetailsContent.setScaleX(barPosition);
			//frameDetailsContent.setScaleY(barPosition);


			float scale = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 1, 0.90);
			float fade = (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 1, 0.05);
			//mainContent.setAlpha(fade);
			mainContent.setScaleX(scale);
			mainContent.setScaleY(scale);



			// Map the spring to the photo grid alpha as it fades to black when the photo is selected.

		}
		else {
			Log.d(TAG, "[BeautifulPhotosActivity - render] - (line 159): " + "container is null");
		}

	}

	@Override
	public void onBackPressed() {
		Log.d(TAG, "[BeautifulPhotosActivity - onBackPressed] - (line 208): " + "fragments: "+getSupportFragmentManager().getFragments().size() );
		if (getSupportFragmentManager().getFragments().size() > 1) {
			mSpring.setEndValue(0);
		} else {
			super.onBackPressed();
		}


	}
}
