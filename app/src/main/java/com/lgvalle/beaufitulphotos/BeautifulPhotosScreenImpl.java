package com.lgvalle.beaufitulphotos;

import android.view.MenuItem;
import android.widget.Toast;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiModule500px;
import com.lgvalle.beaufitulphotos.gallery.DetailsFragment;
import com.lgvalle.beaufitulphotos.gallery.GalleryFragment;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosPresenter;
import com.lgvalle.beaufitulphotos.interfaces.BeautifulPhotosScreen;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
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
public class BeautifulPhotosScreenImpl extends BaseActivity implements BeautifulPhotosScreen {
	/* Manage all business logic for this activity */
	private BeautifulPhotosPresenter presenter;

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
			if (getResources().getBoolean(R.bool.isTablet)) {
				addFragmentToBackStack(R.id.frame_details_content, details);
			} else {
				addFragmentToBackStack(R.id.main_content, details);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// Pop fragments back stack to navigate backwards
				getSupportFragmentManager().popBackStack();
				return true;
		}
		return super.onOptionsItemSelected(item);
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
		// Add Gallery Fragment to main_content frame. If this is a tablet there will be another frame to add content
		GalleryFragment galleryFragment = GalleryFragment.newInstance();
		addFragment(R.id.main_content, galleryFragment);
	}

	@Override
	protected void initPresenter() {
		// Init activity presenter with all it's dependencies
		presenter = new BeautifulPhotosPresenterImpl(this, ApiModule500px.getService());
		// Request data (photos) to activity presenter. Answer will be post on bus, so no need to callbacks here
		presenter.needPhotos();
	}
}
