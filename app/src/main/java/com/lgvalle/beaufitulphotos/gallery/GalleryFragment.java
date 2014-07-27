package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRefreshingEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreElementsEvent;
import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.lgvalle.beaufitulphotos.utils.RendererAdapter;
import com.squareup.otto.Subscribe;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * A PhotoModel Gallery.
 * <p/>
 * This is the UI layer of the Gallery.
 * It is initialized empty and listen for {@link PhotosAvailableEvent} on the bus
 * When a new event is received, all photos are added to the adapter.
 */
public class GalleryFragment extends BaseFragment {
	private static final String TAG = GalleryFragment.class.getSimpleName();
	/* Grid column number is defined in integer.xml, so it depends on screen size */
	@InjectView(R.id.grid_view)
	ListView grid;
	@InjectView(R.id.progress_bar)
	ProgressBar progressBar;

	RendererAdapter<PhotoModel> adapter;

	private int mLastFirstVisibleItem;
	private int columns;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		columns = getActivity().getResources().getInteger(R.integer.column_count);
		// Gallery adapter
		adapter = new RendererAdapter<PhotoModel>(LayoutInflater.from(getActivity()), new GalleryItemRenderer(), getActivity());
	}

	@Override
	public void onResume() {
		super.onResume();
		BusHelper.register(this);
		// Empty list? Ask for some photos!
		if (adapter.isEmpty()) {
			BusHelper.post(new GalleryRequestingMoreElementsEvent());
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		BusHelper.unregister(this);
	}

	public RendererAdapter<PhotoModel> getAdapter() {
		return adapter;
	}

	public static GalleryFragment newInstance() {
		return new GalleryFragment();
	}

	/**
	 * Click on a gallery item
	 *
	 * @param position Position of clicked item
	 */
	@OnItemClick(R.id.grid_view)
	public void onGalleryItemClick(int position) {
		BusHelper.post(new GalleryItemChosenEvent(adapter.getItem(position)));
	}

	/**
	 * Listen to gallery refreshing event.
	 * Event could be triggered from this class or from main activity. That's why it's better to just listen the bus
	 */
	@Subscribe
	public void onGalleryRefreshingEvent(GalleryRefreshingEvent event) {
		adapter.clear();
		setRefreshing(true);
	}

	@Subscribe
	public void onGalleryRequestingMoreElementsEvent(GalleryRequestingMoreElementsEvent event) {
		setRefreshing(true);
	}

	/**
	 * Get notifications when there are new photos available in the bus
	 *
	 * @param event Event containing new photos
	 */
	@Subscribe
	public void onNewPhotosEvent(PhotosAvailableEvent event) {
		if (event != null && event.getPhotos() != null) {
			// Adapter refresh itself
			adapter.addElements(event.getPhotos());

			// Stop refreshing animation
			setRefreshing(false);
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photos_list;
	}

	@Override
	protected void initLayout() {
		// Show app name on actionbar when fragment is ready
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);

		grid.setAdapter(adapter);
		grid.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// nothing to do
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (view.getId() == grid.getId()) {
					final int currentFirstVisibleItem = grid.getFirstVisiblePosition();
					if (currentFirstVisibleItem > mLastFirstVisibleItem) {
						hideActionBar();
					} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
						showActionBar();
					}


					if (mLastFirstVisibleItem != currentFirstVisibleItem) {
						mLastFirstVisibleItem = currentFirstVisibleItem;

					}

					if (!isRefreshing() && grid.getLastVisiblePosition() > adapter.getCount() - columns) {
						Log.d(TAG, "[GalleryFragment - onScrollStateChanged] - (line 151): " + "Need to refresh because: " + grid.getLastVisiblePosition() + " > " + (adapter.getCount() - columns));
						setRefreshing(true);
						BusHelper.post(new GalleryRequestingMoreElementsEvent());
					}
				}
			}
		});


	}

	private boolean isRefreshing() {
		return View.VISIBLE == progressBar.getVisibility();
	}

	private void setRefreshing(boolean refreshing) {
		progressBar.setVisibility(refreshing ? View.VISIBLE : View.GONE);

	}
}
