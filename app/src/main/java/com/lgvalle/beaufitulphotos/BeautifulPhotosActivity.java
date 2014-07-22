package com.lgvalle.beaufitulphotos;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.lgvalle.beaufitulphotos.event.GalleryItemChosenEvent;
import com.lgvalle.beaufitulphotos.fivehundredpxs.ApiModule500px;
import com.lgvalle.beaufitulphotos.gallery.DetailsFragment;
import com.lgvalle.beaufitulphotos.gallery.GalleryFragment;
import com.lgvalle.beaufitulphotos.interfaces.PopularPhotosPresenter;
import com.lgvalle.beaufitulphotos.util.BusHelper;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;


public class BeautifulPhotosActivity extends BaseActivity {
	private static final String TAG = BeautifulPhotosActivity.class.getSimpleName();
	private PopularPhotosPresenter presenter;

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		try {
			File httpCacheDir = new File(getCacheDir(), "https");
			long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
			HttpResponseCache.install(httpCacheDir, httpCacheSize);

		} catch (IOException e) {
			Log.i(TAG, "HTTP response cache installation failed:" + e);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		HttpResponseCache cache = HttpResponseCache.getInstalled();
		if (cache != null) {
			cache.flush();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		BusHelper.register(this);
		BusHelper.register(presenter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		BusHelper.unregister(this);
		BusHelper.unregister(presenter);
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initPresenter() {
		presenter = new BeautifulPhotosPresenterImpl(this, ApiModule500px.getService());
		presenter.needPhotos();
	}

	@Override
	protected void initLayout() {
		GalleryFragment galleryFragment = GalleryFragment.newInstance();
		addFragment(R.id.main_content, galleryFragment);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
		    case android.R.id.home:
			    getSupportFragmentManager().popBackStack();
				return true;
	    }
        return super.onOptionsItemSelected(item);
    }

	@Subscribe
	public void onGalleryItemChosen(GalleryItemChosenEvent event) {
		if (event != null && event.getPhoto() != null) {
			Log.d(TAG, "[PhotosNearbyActivity - onGalleryItemChosen] - (line 84): " + "");
			DetailsFragment details = DetailsFragment.newInstance(event.getPhoto());
			addFragment(R.id.main_content, details);
		}
	}
}
