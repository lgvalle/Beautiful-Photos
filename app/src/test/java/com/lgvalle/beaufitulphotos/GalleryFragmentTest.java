package com.lgvalle.beaufitulphotos;

import com.etsy.android.grid.StaggeredGridView;
import com.lgvalle.beaufitulphotos.fivehundredpxs.model.Photo500px;
import com.lgvalle.beaufitulphotos.gallery.GalleryFragment;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import com.lgvalle.beaufitulphotos.utils.RendererAdapter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by lgvalle on 26/07/14.
 */
@Config(emulateSdk = 18, qualifiers = "v10")
@RunWith(RobolectricTestRunner.class)
public class GalleryFragmentTest extends FragmentTestCase<GalleryFragment> {
	private GalleryFragment fragment;
	private List<PhotoModel> photos;

	@Before
	public void setUp() {
		fragment = GalleryFragment.newInstance();
		photos = new ArrayList<PhotoModel>();
		photos.add(new Photo500px("title1"));
		photos.add(new Photo500px("title2"));
		photos.add(new Photo500px("title3"));
	}

	@Test
	public void createsAndDestroysFragment() {
		startFragment(fragment);
		RendererAdapter<PhotoModel> adapter = fragment.getAdapter();
		assertNotNull(adapter);
		destroyFragment();
	}

	@Test
	public void addElements_shouldUpdateGridViewElementsCount() {
		startFragment(fragment);
		StaggeredGridView gridView = (StaggeredGridView) fragment.getView().findViewById(R.id.grid_view);
		RendererAdapter<PhotoModel> adapter = fragment.getAdapter();
		// Add elements
		adapter.addElements(photos);
		assertEquals(gridView.getCount(), adapter.getCount());
		// Add more elements
		adapter.addElements(photos);
		assertEquals(gridView.getCount(), adapter.getCount());

		destroyFragment();
	}
}
