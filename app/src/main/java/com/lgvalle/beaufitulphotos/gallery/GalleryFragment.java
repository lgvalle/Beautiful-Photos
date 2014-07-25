package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.etsy.android.grid.StaggeredGridView;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRefreshingEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRequestingMoreEvent;
import com.lgvalle.beaufitulphotos.events.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.BusHelper;
import com.lgvalle.beaufitulphotos.utils.RendererAdapter;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 * <p/>
 * A PhotoModel Gallery.
 * <p/>
 * This is the UI layer of the Gallery.
 * It is initialized empty and listen for {@link PhotosAvailableEvent} on the bus
 * When a new event is received, all photos are added to the adapter.
 */
public class GalleryFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
	private static final String TAG = GalleryFragment.class.getSimpleName();
	private RendererAdapter<PhotoModel> adapter;
	private List<PhotoModel> photos;

	/* Grid column number is defined in integer.xml, so it depends on screen size */
	@InjectView(R.id.grid_view)
	StaggeredGridView grid;
	@InjectView(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;
	private int mLastFirstVisibleItem;
	private int columns;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		columns = getActivity().getResources().getInteger(R.integer.column_count);
	}

	@Override
	public void onResume() {
		super.onResume();
		BusHelper.register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		BusHelper.unregister(this);
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
		View v = adapter.getView(position, null, null);

		BusHelper.post(new GalleryItemChosenEvent(photos.get(position), v));
	}

	/**
	 * Get notifications when there are new photos available in the bus
	 *
	 * @param event Event containing new photos
	 */
	@Subscribe
	public void onNewPhotosEvent(PhotosAvailableEvent event) {
		if (event != null && event.getPhotos() != null) {
			// Save photos to display details later
			photos.addAll((List<PhotoModel>) event.getPhotos());
			// Adapter refresh itself
			adapter.addElements(photos);
			// Stop refreshing animation
			swipeLayout.setRefreshing(false);
		}
	}

	/**
	 * Callback when user swipes down to refresh.
	 * Post event in bus asking for refresh.
	 */
	@Override
	public void onRefresh() {
		BusHelper.post(new GalleryRefreshingEvent());
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photos_list;
	}

	@Override
	protected void initLayout() {
		// Show app name on actionbar when fragment is ready
		getActivity().getActionBar().setDisplayShowTitleEnabled(true);
		photos = new ArrayList<PhotoModel>();
		// Swipe to refresh layout
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorSchemeResources(R.color.amber_500, R.color.blue_500, R.color.green_500, R.color.deep_orange_500);
		// Starts in refreshing mode until new items available
		swipeLayout.setRefreshing(true);

		// Gallery adapter
		GalleryItemRenderer renderer = new GalleryItemRenderer();
		adapter = new RendererAdapter<PhotoModel>(LayoutInflater.from(getActivity()), renderer, getActivity());
		grid.setAdapter(adapter);

		grid.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
// TODO Auto-generated method stub

				if (view.getId() == grid.getId()) {
					final int currentFirstVisibleItem = grid.getFirstVisiblePosition();
					if (currentFirstVisibleItem > mLastFirstVisibleItem) {
						getActivity().getActionBar().hide();
					} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
						Log.i("a", "scrolling up...");
						getActivity().getActionBar().show();
					}



					if (mLastFirstVisibleItem != currentFirstVisibleItem) {
						mLastFirstVisibleItem = currentFirstVisibleItem;

					}

					if (grid.getLastVisiblePosition() > adapter.getCount() / 2) {
						swipeLayout.setRefreshing(true);
						BusHelper.post(new GalleryRequestingMoreEvent());
					}
					Log.d(TAG, "[GalleryFragment - onScrollStateChanged] - (line 151): " + "last visible: "+grid.getLastVisiblePosition());



				}
			}

			@Override
			public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int mLastVisibleItem;


			}
		});


	}

	private void loadMoreIfNeeded() {
		Log.d(TAG, "[GalleryFragment - loadMoreIfNeeded] - (line 161): " + "adapter: "+(adapter.getCount() / (columns+1) ));
		Log.d(TAG, "[GalleryFragment - loadMoreIfNeeded] - (line 162): " + "last visible: "+mLastFirstVisibleItem);
		// columns +1 adds a sort of 'padding factor' making request happen earlier
		if (adapter.getCount() > 0 && adapter.getCount() / (columns+1) < mLastFirstVisibleItem) {
			Log.d("a", "[GalleryFragment - loadMoreIfNeeded] - (line 162): " + "request more!");
			swipeLayout.setRefreshing(true);
			BusHelper.post(new GalleryRequestingMoreEvent());
		}

	}

	/**
	 * Listen to gallery refreshing event.
	 * Event could be triggered from this class or from main activity. That's why it's better to just listen the bus
	 */
	@Subscribe
	public void onGalleryRefreshingEvent(GalleryRefreshingEvent event) {
		adapter.clear();
		photos.clear();
		swipeLayout.setRefreshing(true);
	}
}
