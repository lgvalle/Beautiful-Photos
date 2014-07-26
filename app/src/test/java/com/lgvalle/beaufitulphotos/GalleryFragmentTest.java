package com.lgvalle.beaufitulphotos;

import com.lgvalle.beaufitulphotos.gallery.GalleryFragment;
import com.lgvalle.beaufitulphotos.interfaces.PhotoModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.List;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by lgvalle on 26/07/14.
 */
@Config(emulateSdk = 18, qualifiers = "v10")
@RunWith(RobolectricTestRunner.class)
public class GalleryFragmentTest extends FragmentTest<GalleryFragment>{

	private GalleryFragment fragment;


	@Before
	public void setUp() {
		fragment = GalleryFragment.newInstance();
	}

	@Test
	public void createsAndDestroysFragment() {
		startFragment(fragment);
		List<PhotoModel> photos = fragment.getPhotos();
		assertNotNull(photos);
		destroyFragment();
	}

	@Test
	public void pausesAndResumesFragment() {
		startFragment(fragment);
		pauseAndResumeFragment();
		// Assertions go here
	}

	@Test
	public void recreatesFragment() {
		startFragment(fragment);
		fragment = recreateFragment();
		// Assertions go here
	}


}
