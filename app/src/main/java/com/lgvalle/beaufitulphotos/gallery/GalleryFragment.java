package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
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
import com.lgvalle.beaufitulphotos.events.GalleryReloadEvent;
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
	/* Items before list end when loading more elements start */
	private static final int LOAD_OFFSET = 1;
	/* List adapter */
	private RendererAdapter<PhotoModel> adapter;
	/* Save last visible item to know if scrolling up or down */
	private int lastVisible;
	/* Views */
	@InjectView(R.id.photo_list)
	ListView list;
	@InjectView(R.id.progress_bar)
	ProgressBar pbLoading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	@OnItemClick(R.id.photo_list)
	public void onGalleryItemClick(int position) {
		BusHelper.post(new GalleryItemChosenEvent(adapter.getItem(position)));
	}

	/**
	 * Listen to gallery refreshing event.
	 * Event could be triggered from this class or from main activity. That's why it's better to just listen the bus
	 */
	@Subscribe
	public void onGalleryRefreshingEvent(GalleryReloadEvent event) {
		adapter.clear();
		setLoading(true);
	}

	@Subscribe
	public void onGalleryRequestingMoreElementsEvent(GalleryRequestingMoreElementsEvent event) {
		setLoading(true);
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
			setLoading(false);
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

		list.setAdapter(adapter);
		list.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// nothing to do
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (view.getId() == list.getId()) {
					final int currentFirstVisibleItem = list.getFirstVisiblePosition();
					if (currentFirstVisibleItem > lastVisible) {
						hideActionBar();
					} else if (currentFirstVisibleItem < lastVisible) {
						showActionBar();
					}
					lastVisible = currentFirstVisibleItem;


					loadMore();

				}
			}
		});


	}

	/**
	 * Request more items if already scrolled to the end of the list
	 */
	private void loadMore() {
		// Load more items if not already loading
		if (!isLoading() && list.getLastVisiblePosition() >= adapter.getCount() - LOAD_OFFSET) {
			setLoading(true);
			BusHelper.post(new GalleryRequestingMoreElementsEvent());
		}
	}

	/**
	 * @return true if loading progress bar is visible
	 */
	private boolean isLoading() {
		return View.VISIBLE == pbLoading.getVisibility();
	}

	/**
	 * When loading, display progress bar
	 * @param refreshing True if refreshing, false otherwise
	 */
	private void setLoading(boolean refreshing) {
		pbLoading.setVisibility(refreshing ? View.VISIBLE : View.GONE);
	}
}
