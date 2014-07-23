package com.lgvalle.beaufitulphotos.gallery;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.etsy.android.grid.StaggeredGridView;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.events.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.events.GalleryRefreshingEvent;
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
	private RendererAdapter<PhotoModel> adapter;
	private List<PhotoModel> photos;

	/* Grid column number is defined in integer.xml, so it depends on screen size */
	@InjectView(R.id.grid_view)
	StaggeredGridView grid;
	@InjectView(R.id.swipe_container)
	SwipeRefreshLayout swipeLayout;

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
		BusHelper.post(new GalleryItemChosenEvent(photos.get(position)));
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
	 * Post event in bus asking for refresh and clear data.
	 */
	@Override
	public void onRefresh() {
		BusHelper.post(new GalleryRefreshingEvent());
		adapter.clear();
		photos.clear();
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
	}
}
