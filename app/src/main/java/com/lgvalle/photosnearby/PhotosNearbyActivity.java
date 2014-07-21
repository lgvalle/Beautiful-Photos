package com.lgvalle.photosnearby;

import android.view.Menu;
import android.view.MenuItem;
import com.lgvalle.photosnearby.gallery.PhotosFragment;
import com.lgvalle.photosnearby.interfaces.PhotosNearbyPresenter;


public class PhotosNearbyActivity extends BaseActivity {
	private PhotosNearbyPresenter presenter;

	@Override
	protected int getContentView() {
		return R.layout.activity_main;
	}

	@Override
	protected void initPresenter() {
		this.presenter = new PhotosNearbyPresenterImpl(this);
	}

	@Override
	protected void initLayout() {
		PhotosFragment photosFragment = PhotosFragment.newInstance();
		addFragment(R.id.main_content, photosFragment);
	}

	@Override
	protected void onResume() {
		super.onResume();
		presenter.needPhotos();
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
