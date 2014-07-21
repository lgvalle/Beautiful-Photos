package com.lgvalle.photosnearby;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.lgvalle.photosnearby.gallery.PhotosFragment;
import com.lgvalle.photosnearby.interfaces.PhotosNearbyPresenter;
import com.lgvalle.photosnearby.net.ApiModule500px;

import java.io.File;
import java.io.IOException;


public class PhotosNearbyActivity extends BaseActivity {
	private static final String TAG = PhotosNearbyActivity.class.getSimpleName();
	private PhotosNearbyPresenter presenter;

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
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initPresenter() {
		presenter = new PhotosNearbyPresenterImpl(this, ApiModule500px.getService());
		presenter.needPhotos();
	}

	@Override
	protected void initLayout() {
		PhotosFragment photosFragment = PhotosFragment.newInstance();
		addFragment(R.id.main_content, photosFragment);
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
