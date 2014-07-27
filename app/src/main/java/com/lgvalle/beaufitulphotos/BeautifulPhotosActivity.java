package com.lgvalle.beaufitulphotos;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.Api500pxModule;
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
public class BeautifulPhotosActivity extends BaseActivity implements BeautifulPhotosScreen, SlidingUpPanelLayout.PanelSlideListener {
	static final String FRAGMENT_GALLERY_TAG = "fragment_gallery_tag";
	static final String FRAGMENT_DETAILS_TAG = "fragment_details_tag";
	/* Manage all business logic for this activity */
	private BeautifulPhotosPresenter presenter;
	/* Actionbar title */
	private String title;
	/* Actionbar menu */
	private Menu menu;
	/* Views */
	@InjectView(R.id.sliding_layout)
	SlidingUpPanelLayout panel;

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

	@Override
	public void onBackPressed() {
		// If panel is expanded -> collapse
		// If panel is not expanded forward call to super class (making activity close)
		if (!panel.collapsePanel()) {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	/**
	 * Listen to gallery item selection: open panel al request presenter for photo details
	 *
	 * @param event Event containing selected item
	 */
	@Subscribe
	public void onGalleryItemChosen(GalleryItemChosenEvent event) {
		if (event != null && event.getPhoto() != null) {
			panel.expandPanel();
			presenter.needPhotoDetails(event.getPhoto());
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// Collapse panel if open
				panel.collapsePanel();
				break;
			case R.id.action_feature_highest_rated:
				presenter.switchFeature(Feature.HighestRated);
				break;
			case R.id.action_feature_popular:
				presenter.switchFeature(Feature.Popular);
				break;
			case R.id.action_share:
				// Share current photo
				presenter.share(this);
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPanelAnchored(View view) {}

	@Override
	public void onPanelHidden(View view) {}

	@Override
	public void onPanelSlide(View view, float v) {}

	@Override
	public void onPanelCollapsed(View view) {
		// When panel collapsed restore actionbar title and UI elements
		getSupportActionBar().setTitle(title);
		toggleUI(false);
	}

	@Override
	public void onPanelExpanded(View view) {
		// When panel expands (photo selected) always display actionbar with back button
		getSupportActionBar().show();
		toggleUI(true);
	}

	@Override
	public void showError(int errorID) {
		// Sample error managing with a Toast
		Toast.makeText(this, getString(errorID), Toast.LENGTH_SHORT).show();
	}

	/**
	 * Screen api method to update this UI element title
	 * @param titleRes Title resource
	 */
	@Override
	public void updateTitle(int titleRes) {
		title = getString(titleRes);
		getSupportActionBar().setTitle(title);
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initLayout() {
		ButterKnife.inject(this);

		// Listen to details panel to act in actionbar
		panel.setPanelSlideListener(this);
		// Add Gallery Fragment to main_content frame. If this is a tablet there will be another frame to add content
		GalleryFragment galleryFragment = GalleryFragment.newInstance();
		addFragment(R.id.main_content, galleryFragment, FRAGMENT_GALLERY_TAG);

		// Add Details fragment with no content. It's fragment responsibility to listen to item selection events on bus
		DetailsFragment detailsFragment = DetailsFragment.newInstance();
		addFragment(R.id.frame_details_content, detailsFragment, FRAGMENT_DETAILS_TAG);
	}

	@Override
	protected void initPresenter() {
		// Init activity presenter with all it's dependencies
		presenter = new BeautifulPhotosPresenterImpl(this, Api500pxModule.getService(), Feature.Popular);
	}

	/**
	 * Show/Hide elements of UI depending if panel is expanded or not
	 *
	 * @param panelExpanded True if panel is expanded, false otherwise
	 */
	private void toggleUI(boolean panelExpanded) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(panelExpanded);
		menu.findItem(R.id.action_share).setVisible(panelExpanded);
		menu.findItem(R.id.action_feature_popular).setVisible(!panelExpanded);
		menu.findItem(R.id.action_feature_highest_rated).setVisible(!panelExpanded);
	}
}
