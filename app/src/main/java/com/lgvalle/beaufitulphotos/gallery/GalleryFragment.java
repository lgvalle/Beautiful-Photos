package com.lgvalle.beaufitulphotos.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ListView;
import butterknife.InjectView;
import butterknife.OnItemClick;
import com.lgvalle.beaufitulphotos.BaseFragment;
import com.lgvalle.beaufitulphotos.R;
import com.lgvalle.beaufitulphotos.event.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.event.PhotosAvailableEvent;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.util.BusHelper;
import com.lgvalle.beaufitulphotos.util.RendererAdapter;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Created by lgvalle on 21/07/14.
 */
public class GalleryFragment extends BaseFragment implements AbsListView.OnScrollListener{
	private static final String TAG = GalleryFragment.class.getSimpleName();
	private RendererAdapter<PhotoModel> adapter;

	@InjectView(R.id.photos_list)
	ListView list;
	private int preLast;
	private List<PhotoModel> photos;


	public static GalleryFragment newInstance() {
		GalleryFragment f = new GalleryFragment();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
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

	@Override
	public void onScrollStateChanged(AbsListView absListView, int i) {

	}

	@Override
	public void onScroll(AbsListView lw, final int firstVisibleItem,
	                     final int visibleItemCount, final int totalItemCount) {
		switch(lw.getId()) {
			case R.id.photos_list:

				// Make your calculation stuff here. You have all your
				// needed info from the parameters of this function.

				// Sample calculation to determine if the last
				// item is fully visible.
				final int lastItem = firstVisibleItem + visibleItemCount;
				if(lastItem == totalItemCount) {
					if(preLast!=lastItem){ //to avoid multiple calls for last item
						Log.d("Last", "Last");
						preLast = lastItem;
					}
				}
		}

	}

	@Override
	protected void initLayout() {
		GalleryItemRenderer renderer = new GalleryItemRenderer();
		adapter = new RendererAdapter<PhotoModel>(LayoutInflater.from(getActivity()), renderer, getActivity());
		list.setAdapter(adapter);
		list.setOnScrollListener(this);
	}

	@Override
	protected int getContentView() {
		return R.layout.fragment_photos_list;
	}

	@Subscribe
	public void onNewPhotosEvent(PhotosAvailableEvent event) {
		Log.d(TAG, "[PhotosFragment - onNewPhotosEvent] - (line 58): " + "");
		if (event != null && event.getPhotos() != null) {
			photos = (List<PhotoModel>) event.getPhotos();
			adapter.setElements(photos);

		}

	}

	@OnItemClick(R.id.photos_list)
	public void onGalleryItemClick(int position) {
		Log.d(TAG, "[GalleryFragment - onGalleryItemClick] - (line 107): " + position);
		BusHelper.post(new GalleryItemChosenEvent(photos.get(position)));
	}
}
